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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.github.testbot.constans.BotCommands.HELP;
import static com.github.testbot.constans.ChatStates.*;

@Slf4j
@Component
public class CommandParser implements Parser, Commands {

    private final TamTamBotAPI bot;

    private final CustomHttpClient httpClient;

    private final UserService userService;

    private final ConcurrentMap<Long, ChatStates> chatState = new ConcurrentHashMap<>();

    public CommandParser(final TamTamBotAPI bot, final CustomHttpClient httpClient, final UserService userService) {
        this.bot = bot;
        this.httpClient = httpClient;
        this.userService = userService;
    }

    @Override
    public <T extends Update> void parse(@NotNull T update) throws APIException, ClientException {
        MessageCreatedUpdate createdUpdate = (MessageCreatedUpdate) update;
        String messageText = createdUpdate.getMessage().getBody().getText();
        if (messageText == null) {
            log.error("Message text is empty in update {}", update);
            return;
        }
        if (messageText.startsWith("/")) {
            parseCommand(createdUpdate);
        } else {
            try {
                parseDefaultText(createdUpdate);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void parseDefaultText(MessageCreatedUpdate update) throws APIException, ClientException, IOException {
        final String messageText = update.getMessage().getBody().getText();
        final Long senderId = update.getMessage().getSender().getUserId();
        final UserModel user = userService.getUser(senderId);
        if (messageText == null) {
            log.error("Message text is empty in update {}", update);
            return;
        }

        switch (chatState.getOrDefault(senderId, ChatStates.DEFAULT)) {
            case SET_USERNAME:
                user.setGithubUserName(messageText);
                userService.saveUser(user);
                sendSimpleMessage(senderId, "Enter personal access tokens with `admin:repo_hook` scope:");
                chatState.put(senderId, SET_TOKEN);
                break;

            case SET_TOKEN:
                user.setAccessToken(messageText);
                sendSimpleMessage(senderId, "Ok! Token validation...");
                try {
                    if (httpClient.checkAccessTokenForWebhooksOperations(user)) {
                        sendSimpleMessage(senderId, "Success! Now you can subscribe to repos!");
                        user.setLoggedOn(true);
                        userService.saveUser(user);
                    } else {
                        sendSimpleMessage(senderId, "Bad token :(");
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
                    // if the repository already has subscribers, then the webhook for the bot has already been created
                    List<UserModel> githubWebhookUsers = userService.getUsersByGithubRepoName(messageText);
                    GitHubRepositoryModel repository = httpClient.pingGithubRepo(messageText);
                    if (githubWebhookUsers.isEmpty()) {
                        if (user.isLoggedOn()) {
                            Optional<Long> webhookId = httpClient.addWebhookToRepo(user, messageText);
                            if (webhookId.isPresent()) {
                                repository.setWebhookId(webhookId.get());
                                repository.setAccessToken(user.getAccessToken());
                                subscribeToRepo(senderId, user, repository);
                            } else {
                                sendSimpleMessage(senderId, "Problem with setting webhook to repo: "
                                        + messageText);
                            }
                        } else {
                            sendSimpleMessage(senderId,
                                    "You not set github access token. Please, set token.\nCommand: /set_token");
                        }
                    } else {
                        subscribeToRepo(senderId, user, repository);
                    }
                } catch (IOException | SerializationException | IllegalStateException e) {
                    log.error("Send to Github ex", e);
                    sendSimpleMessage(senderId, "Sorry, something " +
                            "went wrong when adding repository \"" + messageText + "\". Check the name is correct");
                } finally {
                    chatState.put(senderId, ChatStates.DEFAULT);
                }
                break;
            case UNSUBSCRIBE_TO_REPO:
                Optional<GitHubRepositoryModel> repositoryModelForRemove = findRepositoryModel(user, messageText);
                if (repositoryModelForRemove.isPresent()) {
                    user.getGithubRepos().remove(repositoryModelForRemove.get());
                    userService.saveUser(user);
                    sendSimpleMessage(senderId, "Unsubscribed from `" + messageText + "` repository");

                    // delete webhook from repository
                    List<UserModel> subscribers = userService.getUsersByGithubRepoName(messageText);
                    if (subscribers.isEmpty()) {
                        if (httpClient.deleteWebhookFromRepository(repositoryModelForRemove.get())) {
                            sendSimpleMessage(senderId, "Webhook of `" + messageText +
                                    "` repository is deleted");
                        } else {
                            log.error("Can not delete webhook from repository: " + messageText);
                        }
                    }
                } else {
                    sendSimpleMessage(senderId, "You are not subscribed to `" + messageText + "` repository!");
                }
                chatState.put(senderId, ChatStates.DEFAULT);
                break;
            case DELETE_REPO_TOKEN:
                Consumer<Optional<GitHubRepositoryModel>> deleteRepositoryConsumer = (gitHubRepositoryModel) -> {
                    chatState.put(senderId, DELETE_CONFIRMATION);
                    try {
                        sendSimpleMessage(senderId, "Are you sure you want to remove webhook " +
                                "from the repository: " + gitHubRepositoryModel.orElseThrow(
                                        () -> new IllegalArgumentException("Repository is null")).getHtmlUrl() +
                                " \n\r" +
                                "Please enter full repository name again if you are sure.\n\r" +
                                "We warn you that everyone who is subscribed to your repository " +
                                "will no longer be able to track it.");
                    } catch (ClientException | APIException e) {
                        log.error("Error while sending message to user", e);
                    }
                };
                deleteByConsumer(deleteRepositoryConsumer, user, messageText);
                break;
            case DELETE_CONFIRMATION:
                Consumer<Optional<GitHubRepositoryModel>> confirmRepositoryConsumer = (gitHubRepositoryModel) ->{
                    try {
                        if (httpClient.deleteWebhookFromRepository(gitHubRepositoryModel.orElseThrow(
                                () -> new IllegalArgumentException("Repository is null")))){
                            userService.getUsersByGithubRepoName(gitHubRepositoryModel.get().getFullName())
                                    .parallelStream()
                                    .forEach(u -> {
                                        u.getGithubRepos().remove(gitHubRepositoryModel.get());
                                        try {
                                            sendSimpleMessage(u.getTamTamUserId(), "Webhook has been removed " +
                                                    "from the repository `" + gitHubRepositoryModel.get().getHtmlUrl()
                                                    + "` by the owner.");
                                        } catch (ClientException | APIException e) {
                                            log.error("Error while sending message to user", e);
                                        }
                                    });
                            sendSimpleMessage(senderId, "Webhook removed from repository `"
                                    + gitHubRepositoryModel.get().getFullName() + "` successfully.");
                        } else {
                            sendSimpleMessage(senderId, "Sorry, webhook not removed from repository `"
                                    + gitHubRepositoryModel.get().getFullName() + "`. Something went wrong. " +
                                    "Manually delete it from the repository");
                        }
                    } catch (IOException e) {
                        log.error("Can not send request to github", e);
                    } catch (APIException | ClientException e) {
                        log.error("Error while sending message to user", e);
                    }
                };
                deleteByConsumer(confirmRepositoryConsumer,user,messageText);
                break;
            default:
                help(senderId);
        }
    }

    private void deleteByConsumer(Consumer<Optional<GitHubRepositoryModel>> consumer, UserModel user,
                                  String repositoryName) throws APIException, ClientException {
        Long senderId = user.getTamTamUserId();
        Optional<GitHubRepositoryModel> repositoryModel = findRepositoryModel(user, repositoryName);
        if (repositoryModel.isPresent()) {
            if (repositoryModel.get().getOwner().getLogin().equals(user.getGithubUserName())) {
                consumer.accept(repositoryModel);
                return;
            } else {
                sendSimpleMessage(senderId, "You are not repository `" + repositoryName + "` owner.\n\r" +
                        "You can not delete webhook.");
            }
        } else {
            sendSimpleMessage(senderId, "You are not subscribed to `" + repositoryName + "` repository!");
        }
        chatState.put(senderId, ChatStates.DEFAULT);

    }

    private Optional<GitHubRepositoryModel> findRepositoryModel(UserModel user, String repositoryName) {
        Set<GitHubRepositoryModel> githubRepositoryModels = user.getGithubRepos();
        Predicate<GitHubRepositoryModel> forRemove = repo ->
                repositoryName.equals(repo.getFullName()) || repositoryName.equals(repo.getName());
        return githubRepositoryModels.stream()
                .filter(forRemove)
                .findFirst();
    }


    @SuppressWarnings("Duplicates")
    private void parseCommand(MessageCreatedUpdate update) throws APIException, ClientException {
        final String messageText = update.getMessage().getBody().getText();
        final Long senderId = update.getMessage().getSender().getUserId();
        if (messageText == null) {
            log.error("Message text is empty in update {}", update);
            return;
        }
        final String commandBody = messageText.substring(1).toUpperCase();
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
            case SET_TOKEN:
                chatState.put(senderId, SET_USERNAME);
                sendSimpleMessage(senderId, "Enter your GitHub username:");
                break;
            case SUBSCRIBE:
                log.info("UPD " + update.toString());
                chatState.put(senderId, SUBSCRIBE_TO_REPO);
                sendSimpleMessage(senderId, "Enter full GitHub repository name (username/repository)");
                break;
            case UNSUBSCRIBE:
                chatState.put(senderId, UNSUBSCRIBE_TO_REPO);
                sendSimpleMessage(senderId, "Enter GitHub repository name for unsubscribe");
                break;
            case DELETE:
                chatState.put(senderId, DELETE_REPO_TOKEN);
                sendSimpleMessage(senderId, "Enter GitHub repository name for delete webhook");
                break;
            case LIST:
                chatState.put(senderId, ChatStates.DEFAULT);
                list(senderId);
                break;

            default:
                NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, update.getMessage().getBody().getMid());
                NewMessageBody body = new NewMessageBody("Sorry, I don't know command: " + update.getMessage().getBody().getText(), null, link);
                try {
                    bot.sendMessage(body).userId(senderId).execute();
                } catch (ClientException | APIException e) {
                    log.error("Can not send default message", e);
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
            builder.append("List of your connected repositories:\n\n");
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
