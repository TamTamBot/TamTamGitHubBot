package com.github.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.testbot.interfaces.BotActions;

public class WebhookBot implements BotActions {

    private TamTamBotAPI bot;

    public WebhookBot(TamTamBotAPI bot) throws APIException, ClientException {
        this.bot = bot;
    }

    public void setWebhook(String webhookUrl) throws ClientException, APIException {
        bot.subscribe(new SubscriptionRequestBody(webhookUrl)).execute();
    }

    @Override
    public void onMessageCreate(MessageCreatedUpdate update) {
        Message message = update.getMessage();
        User sender = update.getMessage().getSender();
        NewMessageBody body = new NewMessageBody( message.getBody().getText() + " Hello," + sender, null, null);
        try {
            bot.sendMessage(body).userId(sender.getUserId()).execute();
        } catch (ClientException | APIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleUpdate(Update update) {
        switch (update.getType()) {
            case (Update.BOT_ADDED):
                onBotAddedToChat((BotAddedToChatUpdate) update);
                break;
            case (Update.BOT_STARTED):
                onBotStarted((BotStartedUpdate) update);
                break;
            case (Update.BOT_REMOVED):
                onBotRemovedFromChat((BotRemovedFromChatUpdate) update);
                break;
            case (Update.MESSAGE_CALLBACK):
                onMessageCallback((MessageCallbackUpdate) update);
                break;
            case (Update.MESSAGE_CREATED):
                onMessageCreate((MessageCreatedUpdate) update);
                break;
            case (Update.MESSAGE_EDITED):
                onMessageEdited((MessageEditedUpdate) update);
                break;
            case (Update.MESSAGE_REMOVED):
                onMessageRemoved((MessageRemovedUpdate) update);
                break;
            case (Update.USER_ADDED):
                onUserAddedToChat((UserAddedToChatUpdate) update);
                break;
            case (Update.USER_REMOVED):
                onUserRemovedFromChat((UserRemovedFromChatUpdate) update);
                break;
            case (Update.CHAT_TITLE_CHANGED):
                onChatTitleChanged((ChatTitleChangedUpdate) update);
        }
    }

    @Override
    public void onMessageCallback(MessageCallbackUpdate update) {

    }

    @Override
    public void onMessageEdited(MessageEditedUpdate update) {

    }

    @Override
    public void onMessageRemoved(MessageRemovedUpdate update) {

    }

    @Override
    public void onBotAddedToChat(BotAddedToChatUpdate update) {

    }

    @Override
    public void onBotStarted(BotStartedUpdate update) {

    }

    @Override
    public void onBotRemovedFromChat(BotRemovedFromChatUpdate update) {

    }

    @Override
    public void onUserAddedToChat(UserAddedToChatUpdate userAddedToChatUpdate) {

    }

    @Override
    public void onUserRemovedFromChat(UserRemovedFromChatUpdate userAddedToChatUpdate) {

    }

    @Override
    public void onChatTitleChanged(ChatTitleChangedUpdate update) {

    }


}
