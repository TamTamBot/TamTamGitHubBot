package com.github.testbot.interfaces;

import chat.tamtam.botapi.model.*;

public interface BotActions {
    public void onMessageCreate(MessageCreatedUpdate update);

    public void onMessageCallback(MessageCallbackUpdate update);

    public void onMessageEdited(MessageEditedUpdate update);

    public void onMessageRemoved(MessageRemovedUpdate update);

    public void onBotAddedToChat(BotAddedToChatUpdate update);

    public void onBotStarted(BotStartedUpdate update);

    public void onBotRemovedFromChat(BotRemovedFromChatUpdate update);

    public void onUserAddedToChat(UserAddedToChatUpdate userAddedToChatUpdate);

    public void onUserRemovedFromChat(UserRemovedFromChatUpdate userAddedToChatUpdate);

    public void onChatTitleChanged(ChatTitleChangedUpdate update);

    public void handleUpdate(Update update);
//    {
//
//        switch (update.getType()) {
//            case (Update.BOT_ADDED):
//                onBotAddedToChat((BotAddedToChatUpdate) update);
//                break;
//            case (Update.BOT_STARTED):
//                onBotStarted((BotStartedUpdate) update);
//                break;
//            case (Update.BOT_REMOVED):
//                onBotRemovedFromChat((BotRemovedFromChatUpdate) update);
//                break;
//            case (Update.MESSAGE_CALLBACK):
//                onMessageCallback((MessageCallbackUpdate) update);
//                break;
//            case (Update.MESSAGE_CREATED):
//                onMessageCreate((MessageCreatedUpdate) update);
//                break;
//            case (Update.MESSAGE_EDITED):
//                onMessageEdited((MessageEditedUpdate) update);
//                break;
//            case (Update.MESSAGE_REMOVED):
//                onMessageRemoved((MessageRemovedUpdate) update);
//                break;
//            case (Update.USER_ADDED):
//                onUserAddedToChat((UserAddedToChatUpdate) update);
//                break;
//            case (Update.USER_REMOVED):
//                onUserRemovedFromChat((UserRemovedFromChatUpdate) update);
//                break;
//            case (Update.CHAT_TITLE_CHANGED):
//                onChatTitleChanged((ChatTitleChangedUpdate) update);
//        }
//    }
}
