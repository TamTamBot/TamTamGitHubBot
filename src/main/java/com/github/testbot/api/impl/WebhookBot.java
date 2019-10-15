package com.github.testbot.api.impl;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.testbot.api.AbstractBotAPI;

public class WebhookBot extends AbstractBotAPI {

    private TamTamBotAPI botAPI;

    public WebhookBot(TamTamBotAPI bot) throws APIException, ClientException {
        super(bot);
        this.botAPI = bot;
    }

    @Override
    public void onMessageCreate(MessageCreatedUpdate update) {
        Message message = update.getMessage();
        User sender = update.getMessage().getSender();
        NewMessageBody body = new NewMessageBody( message.getBody().getText() + " Hello," + sender, null, null);
        try {
            botAPI.sendMessage(body).userId(sender.getUserId()).execute();
        } catch (ClientException | APIException e) {
            e.printStackTrace();
        }

    }
}
