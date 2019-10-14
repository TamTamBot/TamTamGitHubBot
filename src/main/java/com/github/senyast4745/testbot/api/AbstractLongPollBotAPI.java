package com.github.senyast4745.testbot.api;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.TamTamClient;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.client.impl.OkHttpTransportClient;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;

public abstract class AbstractLongPollBotAPI {
    public final TamTamBotAPI bot;

    public AbstractLongPollBotAPI(TamTamBotAPI bot) throws APIException, ClientException {
        this.bot = bot;


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

    public void start() throws APIException, ClientException {
        Long marker = null;

        bot.getUpdates().marker(null).execute();

        while (true) {
            UpdateList updateList = bot.getUpdates().marker(marker).execute();
            marker = updateList.getMarker();
            updateList.getUpdates().forEach(update -> {
                System.out.println(update);
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
                        onMessageCrete((MessageCreatedUpdate) update);
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


            });
        }
    }

}

