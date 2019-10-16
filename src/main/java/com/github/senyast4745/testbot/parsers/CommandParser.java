package com.github.senyast4745.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.senyast4745.testbot.constans.CommandStates;
import com.github.senyast4745.testbot.constans.Commands;
import com.github.senyast4745.testbot.repos.GitHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.senyast4745.testbot.constans.Callbacks.*;
import static com.github.senyast4745.testbot.constans.CommandStates.DEFAULT;
import static com.github.senyast4745.testbot.constans.CommandStates.REG_STATE;

public class CommandParser {

    private Logger log = LoggerFactory.getLogger(CommandParser.class);

    private TamTamBotAPI bot;

    ConcurrentMap<Long, CommandStates> chatState = new ConcurrentHashMap<>();

    public CommandParser(TamTamBotAPI bot) {
        this.bot = bot;
    }

    public void parseCommand(MessageCreatedUpdate update) throws APIException, ClientException, SQLException {
        String command = update.getMessage().getBody().getText();
        Long chatId = update.getMessage().getRecipient().getChatId();
        Long senderId = update.getMessage().getSender().getUserId();
        assert chatId != null;
        assert command != null;
        if (command.startsWith("/")) {
            String commandBody = command.split(" ")[0];
            commandBody = commandBody.substring(1).toUpperCase();
            log.info("Command " + commandBody);
//            log.info(Commands);
            Commands commandEnum = Commands.NOT_FOUND;
            try {
                commandEnum = Commands.valueOf(commandBody);
            } catch (IllegalArgumentException ignored) {
                log.info("Command not fount");
            }
            switch (commandEnum) {
                case HELP:
                    chatState.put(senderId, CommandStates.DEFAULT);
                    help(chatId);
                    break;
                case REG:
                    chatState.put(senderId, REG_STATE);
                    log.info("UPD " + update.toString());
                    registration(chatId);
                    break;
                default:
                    NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, update.getMessage().getBody().getMid());
                    NewMessageBody body = new NewMessageBody("Sorry, I don't know command: " + update.getMessage().getBody().getText(), null, link);
                    try {
                        bot.sendMessage(body).chatId(chatId).execute();
                    } catch (ClientException | APIException e) {
                        e.printStackTrace();
                    }

            }
        } else {
            switch (chatState.getOrDefault(senderId, CommandStates.DEFAULT)) {
                case REG_STATE:
                    GitHubUser.insertNewTamTamUser(update.getMessage().getSender().getUserId(),
                            command);

                    chatState.put(senderId, DEFAULT);
                    sendSimpleMessage(chatId, "Added");
                    break;
                default:
                    help(chatId);
            }
        }
    }

    private void help(long chatId) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody("help command",
                Arrays.asList(new InlineKeyboardAttachmentRequest(
                        new InlineKeyboardAttachmentRequestPayload(
                                Arrays.asList(
                                        Arrays.asList(
                                                new CallbackButton(HELP.name(), "help")))))), null))
                .chatId(chatId).execute();
    }

    private void registration(long chatId) throws ClientException, APIException {
        sendSimpleMessage(chatId,"Enter full GitHub repository name");
    }

    private void sendSimpleMessage(long chatId, String message) throws ClientException, APIException {
        bot.sendMessage(new NewMessageBody(message, null, null))
                .chatId(chatId).execute();
    }
}
