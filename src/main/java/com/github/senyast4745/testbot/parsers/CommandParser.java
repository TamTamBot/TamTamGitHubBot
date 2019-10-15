package com.github.senyast4745.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.enums.Callbacks;
import com.github.senyast4745.testbot.enums.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Function;

import static com.github.senyast4745.testbot.enums.Callbacks.*;

public class CommandParser {

    private Logger log = LoggerFactory.getLogger(CommandParser.class);

    private TamTamBotAPI bot;

    public CommandParser(TamTamBotAPI bot) {
        this.bot = bot;
    }

    public void parseCommand(MessageCreatedUpdate update) throws APIException, ClientException {
        String command = update.getMessage().getBody().getText();
        Long chatId = update.getMessage().getRecipient().getChatId();
        assert chatId != null;
        assert command != null;
        if(command.startsWith("/")) {
            command  = command.substring(1).toUpperCase();
            log.info("Command " + command);
//            log.info(Commands);

            switch (Commands.valueOf(command)) {
                case HELP:
                    help(chatId);
                case REG:
                    log.info("Say Hello");
                    break;
                default:
                    NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, update.getMessage().getBody().getMid());
                    NewMessageBody body = new NewMessageBody("Your text msg: " + update.getMessage().getBody().getText(), null, link);
                    log.info(Thread.currentThread().getName());
                    try {
                        bot.sendMessage(body).chatId(chatId).execute();
                    } catch (ClientException | APIException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            log.info("Unknown command " + command);
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
}
