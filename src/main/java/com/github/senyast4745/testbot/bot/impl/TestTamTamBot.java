package com.github.senyast4745.testbot.bot.impl;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.senyast4745.testbot.bot.TamTamBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class TestTamTamBot extends TamTamBot {

    TamTamBotAPI bot;

    Logger log = LoggerFactory.getLogger(TestTamTamBot.class);


    public TestTamTamBot(String serverURL, String token) throws ClientException, APIException {
        super(serverURL, token);
        bot = getBot();
    }

    @Override
    public void onMessageCrete(MessageCreatedUpdate update) {

        update.getMessage().getBody();
        NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, update.getMessage().getBody().getMid());
        NewMessageBody body = new NewMessageBody("Your text msg: " + update.getMessage().getBody().getText(), null, link);
        Long chatId = update.getMessage().getRecipient().getChatId();
        assert chatId != null;
        log.info( Thread.currentThread().getName());
        try {
            bot.sendMessage(body).chatId(chatId).execute();
        } catch (ClientException | APIException e) {
            e.printStackTrace();
        }
    }
}
