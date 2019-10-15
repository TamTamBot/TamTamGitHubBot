package com.github.senyast4745.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;

public abstract class TamTamBot {

    private TamTamBotAPI bot;

    public TamTamBot(String serverURL, String token) throws ClientException, APIException {
        bot = TamTamBotAPI.create(token);
        bot.subscribe(new SubscriptionRequestBody(serverURL)).execute();
    }

    protected TamTamBotAPI getBot() {
        return bot;
    }

    public void onMessageCrete(MessageCreatedUpdate update) {
    }

    public void onMessageCallback(MessageCallbackUpdate update) {
    }

    public void onMessageEdited(MessageEditedUpdate update) {
    }

    public void onMessageRemoved(MessageRemovedUpdate update) {
    }

    public void onBotAddedToChat(BotAddedToChatUpdate update) {
    }

    public void onBotStarted(BotStartedUpdate update) {
    }

    public void onBotRemovedFromChat(BotRemovedFromChatUpdate update) {
    }

    public void onUserAddedToChat(UserAddedToChatUpdate userAddedToChatUpdate) {
    }

    public void onUserRemovedFromChat(UserRemovedFromChatUpdate userAddedToChatUpdate) {
    }

    public void onChatTitleChanged(ChatTitleChangedUpdate update) {
    }
}
