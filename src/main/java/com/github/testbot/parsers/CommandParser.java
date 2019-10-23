package com.github.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.exceptions.SerializationException;
import chat.tamtam.botapi.model.*;
import com.github.testbot.constans.BotCommands;
import com.github.testbot.constans.ChatStates;
import com.github.testbot.github.CustomHttpClient;
import com.github.testbot.interfaces.BotTexts;
import com.github.testbot.interfaces.Commands;
import com.github.testbot.interfaces.Parser;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.GitHubRepositoryModel;
import com.github.testbot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.testbot.constans.BotCommands.HELP;
import static com.github.testbot.constans.ChatStates.*;

@Slf4j
@Component
public class CommandParser implements Parser, Commands {

    private final TamTamBotAPI bot;

    private final CustomHttpClient httpClient;

    private final UserService userService;

    private final ConcurrentMap<Long, ChatStates> chatState = new ConcurrentHashMap<>();

    public CommandParser(final TamTamBotAPI bot, final CustomHttpClient httpClient, UserService userService) {
        this.bot = bot;
        this.httpClient = httpClient;
        this.userService = userService;
    }

    @Override
    public <T extends Update> void parse(@NotNull T update) throws APIException, ClientException {
        MessageCreatedUpdate createdUpdate = (MessageCreatedUpdate) update;
        String messageText = createdUpdate.getMessage().getBody().getText();
        Long senderId = createdUpdate.getMessage().getSender().getUserId();
        UserModel user = userService.getUser(senderId);
        if (messageText == null) {
            log.error("Message text is empty in update {}", update);
            return;
        }
        if (messageText.startsWith("/")) {
            String commandBody = messageText.substring(1).toUpperCase();
            log.info("Command " + commandBody);
            BotCommands commandEnum = BotCommands.NOT_FOUND;
            try {
                commandEnum = BotCommands.valueOf(commandBody);
            } catch (IllegalArgumentException ignored) {
                log.info("Command not fount");
            }
            switch (commandEnum) {
                case HELP:
                    chatState.put(senderId, ChatStates.DEFAULT);
                    help(senderId);
                    break;
                case LOGIN:
                    chatState.put(senderId, LOGIN_USERNAME);
                    sendSimpleMessage(senderId, "Enter your GitHub username");
                    break;
                case SUBSCRIBE:
                    log.info("UPD " + update.toString());
                    chatState.put(senderId, SUBSCRIBE_TO_REPO);
                    sendSimpleMessage(senderId, "Enter full GitHub repository name (username/repository)");
                    break;
                case LIST:
                    chatState.put(senderId, ChatStates.DEFAULT);
                    list(senderId);
                    break;

                default:
                    NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, createdUpdate.getMessage().getBody().getMid());
                    NewMessageBody body = new NewMessageBody("Sorry, I don't know command: " + createdUpdate.getMessage().getBody().getText(), null, link);
                    try {
                        bot.sendMessage(body).userId(senderId).execute();
                    } catch (ClientException | APIException e) {
                        e.printStackTrace();
                    }

            }
        } else {
            switch (chatState.getOrDefault(senderId, ChatStates.DEFAULT)) {
                case LOGIN_USERNAME:
                    user.setGithubUserName(messageText);
                    userService.saveUser(user);
                    sendSimpleMessage(senderId, "Enter your GitHub password");
                    chatState.put(senderId, LOGIN_PASSWORD);
                    break;
                case LOGIN_PASSWORD:
                    user.setGithubPassword(messageText);
                    sendSimpleMessage(senderId, "Ok! Check creds...");
                    try {
                        if (httpClient.checkGithubCredentials(user)) {
                            sendSimpleMessage(senderId, "Success! Now you can subscribe to repos!");
                            user.setLoggedOn(true);
                            userService.saveUser(user);
                        } else {
                            sendSimpleMessage(senderId, "Bad credentials :( Plz, try to login with another creds");
                            user.setLoggedOn(false);
                            userService.saveUser(user);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        chatState.put(senderId, ChatStates.DEFAULT);
                    }
                    break;
                case SUBSCRIBE_TO_REPO:
                    try {
                        List<UserModel> githubWebhookUsers = userService.getUsersByGithubRepoName(messageText);
                        GitHubRepositoryModel repository = httpClient.pingGithubRepo(messageText);
                        if (githubWebhookUsers.isEmpty()) {
                            if (user.isLoggedOn()) {
                                if (httpClient.addWebhookToRepo(user, messageText)) {
                                    subscribeToRepo(senderId, user, repository);
                                } else {
                                    sendSimpleMessage(senderId, "Problem with setting webhook to repo: " + messageText);
                                }
                            } else {
                                sendSimpleMessage(senderId, "You not logged in github. Please, login to github.\nCommand: /login");
                            }
                        } else {
                            subscribeToRepo(senderId, user, repository);
                        }
                    } catch (IOException | SerializationException | IllegalStateException  e) {
                        log.error("Send to Github ex", e);
                        sendSimpleMessage(senderId, "Sorry, something " +
                                "went wrong when adding repository \"" + messageText + "\". Check the name is correct");
                    } finally {
                        chatState.put(senderId, ChatStates.DEFAULT);
                    }
                    break;
                default:
                    help(senderId);
            }
        }

    }

    @Override
    public void help(long senderId) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody(BotTexts.HELP_COMMAND_TEXT,
                Collections.singletonList(new InlineKeyboardAttachmentRequest(
                        new InlineKeyboardAttachmentRequestPayload(
                                Collections.singletonList(
                                        Collections.singletonList(
                                                new CallbackButton(HELP.name(), BotTexts.MORE_INFO_CALLBACK_BUTTON)))))), null))
                .userId(senderId).execute();
    }


    @Override
    public void subscribeToRepo(long senderId, UserModel user, GitHubRepositoryModel repository)
            throws ClientException, APIException {
        user.addRepositoryToSubscriptions(repository);
        userService.saveUser(user);
        chatState.put(senderId, DEFAULT);
        sendSimpleMessage(senderId, "Successful subscription to repo: " + repository.getName());
    }

    @Override
    public void list(long senderId) throws APIException, ClientException {
        StringBuilder builder = new StringBuilder();
        UserModel user = userService.getUser(senderId);
        Set<GitHubRepositoryModel> userSubscriptions = user.getGithubRepos();
        if (userSubscriptions.isEmpty()) {
            builder.append("List of your connected repositories is empty!");
        } else {
            builder.append("List of your connected repositories:\n\r\n");
            user.getGithubRepos().forEach(gitHubRepositoryModel -> {
                log.info("REPO " + user);
                builder.append("name: ").append(gitHubRepositoryModel.getFullName()).append("\n\rurl: ")
                        .append(gitHubRepositoryModel.getHtmlUrl()).append("\n\n");
            });
        }
        builder.append("\nTo add new repositories use command /subscribe");
        log.info("LIST " + builder.toString());
        sendSimpleMessage(senderId, builder.toString());
    }

    @Override
    public void sendSimpleMessage(long senderId, String message) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody(message, null, null))
                .userId(senderId).execute();
    }

}
