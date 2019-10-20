package com.github.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.exceptions.SerializationException;
import chat.tamtam.botapi.model.*;
import com.github.testbot.constans.CommandStates;
import com.github.testbot.constans.BotCommands;
import com.github.testbot.github.CustomHttpClient;
import com.github.testbot.interfaces.Commands;
import com.github.testbot.interfaces.Parser;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.GitHubRepositoryModel;
import com.github.testbot.repository.MongoUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.testbot.constans.BotCommands.HELP;
import static com.github.testbot.constans.CommandStates.*;

@Slf4j
@Component
public class CommandParser implements Parser, Commands {
    private final TamTamBotAPI bot;

    private final CustomHttpClient httpClient;

    private final
    MongoUserRepository userRepository;

    private final ConcurrentMap<Long, CommandStates> chatState = new ConcurrentHashMap<>();

    public CommandParser(final TamTamBotAPI bot, final CustomHttpClient httpClient, final MongoUserRepository userRepository) {
        this.bot = bot;
        this.httpClient = httpClient;
        this.userRepository = userRepository;
    }

    @Override
    public <T extends Update> void parse(@NotNull T update) throws APIException, ClientException {
        MessageCreatedUpdate createdUpdate = (MessageCreatedUpdate) update;
        String command = createdUpdate.getMessage().getBody().getText();
        Long senderId = createdUpdate.getMessage().getSender().getUserId();
        assert senderId != null;
        assert command != null;
        if (command.startsWith("/")) {
            String commandBody = command.split(" ")[0];
            commandBody = commandBody.substring(1).toUpperCase();
            log.info("Command " + commandBody);
            BotCommands commandEnum = BotCommands.NOT_FOUND;
            try {
                commandEnum = BotCommands.valueOf(commandBody);
            } catch (IllegalArgumentException ignored) {
                log.info("Command not fount");
            }
            switch (commandEnum) {
                case HELP:
                    chatState.put(senderId, CommandStates.DEFAULT);
                    help(senderId);
                    break;
                case REG:
                    chatState.put(senderId, REG_STATE_1);
                    log.info("UPD " + update.toString());
                    registration(senderId);
                    break;
                case LIST:
                    chatState.put(senderId, CommandStates.DEFAULT);
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
            switch (chatState.getOrDefault(senderId, CommandStates.DEFAULT)) {
                case REG_STATE_1:
                    try {
                        GitHubRepositoryModel repository = httpClient.pingGithubRepo(command);
                        userRepository.save(new UserModel(senderId, repository));
                        chatState.put(senderId, DEFAULT);
                        sendSimpleMessage(senderId, "Added");
                    } catch (IOException | SerializationException | IllegalStateException  e) {
                        log.error("Send to Github ex", e);
                        sendSimpleMessage(senderId, "Sorry, something " +
                                "went wrong when adding repository \"" + command + "\". Check the name is correct" +
                                " and is webhooks are enabled for our repository");
                    }

                    break;
                default:
                    help(senderId);
            }
        }

    }

    @Override
    public void help(long senderId) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody("help command",
                Arrays.asList(new InlineKeyboardAttachmentRequest(
                        new InlineKeyboardAttachmentRequestPayload(
                                Arrays.asList(
                                        Arrays.asList(
                                                new CallbackButton(HELP.name(), "help")))))), null))
                .userId(senderId).execute();
    }

    @Override
    public void registration(long senderId) throws ClientException, APIException {
        sendSimpleMessage(senderId, "Enter full GitHub repository name");
    }

    @Override
    public void list(long senderId) throws APIException, ClientException {
        StringBuilder builder = new StringBuilder("List of your connected repositories:\n\r");
        userRepository.findAllByTamTamUserId(senderId).forEach(user -> {
            log.info("REPO " + user);
            builder.append("name: ").append(user.getRepository().getFullName()).append("\n\r  url: ")
                    .append(user.getRepository().getHtmlUrl()).append("\n\r");
        });
        builder.append("To add new repositories use command /reg");
        log.info("LIST " + builder.toString());
        sendSimpleMessage(senderId, builder.toString());
    }

    @Override
    public void sendSimpleMessage(long senderId, String message) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody(message, null, null))
                .userId(senderId).execute();
    }

}
