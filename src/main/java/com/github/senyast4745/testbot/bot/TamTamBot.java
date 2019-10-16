package com.github.senyast4745.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.senyast4745.testbot.constans.Commands;

import java.util.Arrays;
import java.util.List;

import static com.github.senyast4745.testbot.constans.Commands.*;
public abstract class TamTamBot {

    private static TamTamBotAPI bot;

    public TamTamBot(String serverURL, String token) throws ClientException, APIException {
        bot = TamTamBotAPI.create(token);
        bot.subscribe(new SubscriptionRequestBody(serverURL)).execute();
        List<BotCommand> commands = Arrays.asList(
                new BotCommand(HELP.getCommandName()).description(HELP.getCommandName()),
                new BotCommand(REG.getCommandName()).description(REG.getDescription()),
                new BotCommand(LIST.getCommandName()).description(LIST.getDescription()),
                new BotCommand("hello").description("Say Hello to World"));
        bot.editMyInfo(new BotPatch().name("MyBot")
                .username("testGitBot").description("Test bot to Tam Tam").commands(commands)).execute();

    }

    protected static TamTamBotAPI getBot() {
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
