package com.github.senyast4745.testbot.api.impl;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.MessageBody;
import chat.tamtam.botapi.model.MessageCreatedUpdate;
import chat.tamtam.botapi.model.NewMessageBody;
import com.github.senyast4745.testbot.api.AbstractLongPollBotAPI;

public class LongPollBot extends AbstractLongPollBotAPI {

    private TamTamBotAPI botAPI;

    public LongPollBot(TamTamBotAPI bot) throws APIException, ClientException {
        super(bot);
        this.botAPI = bot;
    }

    @Override
    public void onMessageCrete(MessageCreatedUpdate update) {
        MessageBody bodyR = update.getMessage().getBody();
        NewMessageBody body = new NewMessageBody( bodyR.getText() + " Hello," + update.getMessage().getSender(), null, null);
        try {
            botAPI.sendMessage(body).userId(update.getMessage().getSender().getUserId()).execute();
        } catch (ClientException | APIException e) {
            e.printStackTrace();
        }

    }
}
