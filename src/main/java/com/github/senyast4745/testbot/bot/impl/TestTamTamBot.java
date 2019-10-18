package com.github.senyast4745.testbot.bot.impl;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.parsers.CallbackParser;
import com.github.senyast4745.testbot.parsers.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class TestTamTamBot extends TamTamBot {

    private TamTamBotAPI bot;

    private Logger log = LoggerFactory.getLogger(TestTamTamBot.class);

    private CommandParser commandParser;
    private CallbackParser callbackParser;

    public TestTamTamBot(@Value("server.bot.url") String serverURL, @Value("bot.token") String token) throws ClientException, APIException {
        super(serverURL, token);
        bot = getBot();
        commandParser = new CommandParser(bot);
        callbackParser = new CallbackParser(bot);

    }

    @Override
    public void onMessageCrete(MessageCreatedUpdate update) {

        try {
            commandParser.parseCommand(update);
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*update.getMessage().getBody();
        NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, update.getMessage().getBody().getMid());
        NewMessageBody body = new NewMessageBody("Your text msg: " + update.getMessage().getBody().getText(), null, link);
        Long chatId = update.getMessage().getRecipient().getChatId();
        assert chatId != null;
        log.info(Thread.currentThread().getName());
        try {
            bot.sendMessage(body).chatId(chatId).execute();
        } catch (ClientException | APIException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onMessageCallback(MessageCallbackUpdate update) {
        try {
            callbackParser.parseCallback(update);
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
