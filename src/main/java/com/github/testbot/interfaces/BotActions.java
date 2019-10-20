package com.github.testbot.interfaces;

import chat.tamtam.botapi.model.*;

public interface BotActions {

    void onMessageCreate(MessageCreatedUpdate update);

    void onMessageCallback(MessageCallbackUpdate update);

    void onMessageEdited(MessageEditedUpdate update);

    void onMessageRemoved(MessageRemovedUpdate update);

    void onBotAddedToChat(BotAddedToChatUpdate update);

    void onBotStarted(BotStartedUpdate update);

    void onBotRemovedFromChat(BotRemovedFromChatUpdate update);

    void onUserAddedToChat(UserAddedToChatUpdate userAddedToChatUpdate);

    void onUserRemovedFromChat(UserRemovedFromChatUpdate userAddedToChatUpdate);

    void onChatTitleChanged(ChatTitleChangedUpdate update);

    void handleUpdate(Update update);
}
