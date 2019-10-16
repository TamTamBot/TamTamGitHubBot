package com.github.senyast4745.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;

import com.github.senyast4745.testbot.constans.Callbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackParser {

    private Logger log = LoggerFactory.getLogger(CommandParser.class);

    private TamTamBotAPI bot;

    public CallbackParser(TamTamBotAPI bot) {
        this.bot = bot;
    }

    public void parseCallback(MessageCallbackUpdate update) throws APIException, ClientException {
        update.getCallback().getPayload();

        String command = update.getCallback().getPayload();
        String callbackId = update.getCallback().getCallbackId();
        //        Long id = update.getMessage().getRecipient().getChatId();
//        Long chatId = update.getMessage().getRecipient().getChatId();
//        assert userId != null;
        assert command != null;

        switch (Callbacks.valueOf(command.toUpperCase())) {
            case HELP:
                help(callbackId);
            default:

        }
    }

    private void help(String callbackId) throws ClientException, APIException {
        CallbackAnswer answer = new CallbackAnswer().message(new NewMessageBody("hello calback", null, null));
        bot.answerOnCallback(answer, callbackId).execute();
    }
}
