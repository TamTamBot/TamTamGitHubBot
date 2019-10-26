package com.github.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.github.testbot.interfaces.BotActions;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.parsers.CallbackParser;
import com.github.testbot.parsers.CommandParser;
import com.github.testbot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.github.testbot.constans.BotCommands.*;

@Slf4j
public class WebhookBot implements BotActions {

    private TamTamBotAPI bot;

    @Autowired
    private CommandParser commandParser;
    @Autowired
    private CallbackParser callbackParser;

    @Autowired
    private UserService userService;

    /**
     * Service class for handle tam tam events.
     *
     * @param bot bot API for interaction with Tam Tam servers
     * @throws APIException    with errors occur while interacting with API
     * @throws ClientException if errors occur during the sending of the request
     */
    public WebhookBot(TamTamBotAPI bot) throws APIException, ClientException {
        this.bot = bot;
        List<BotCommand> commands = Arrays.asList(
                new BotCommand(SET_TOKEN.getCommandName()).description(SET_TOKEN.getDescription()),
                new BotCommand(SUBSCRIBE.getCommandName()).description(SUBSCRIBE.getDescription()),
                new BotCommand(LIST.getCommandName()).description(LIST.getDescription()),
                new BotCommand(HELP.getCommandName()).description(HELP.getDescription()));
        bot.editMyInfo(new BotPatch().name("GithubNotifyBot").commands(commands)).execute();
    }

    public void setWebhook(String webhookUrl) throws ClientException, APIException {
        bot.subscribe(new SubscriptionRequestBody(webhookUrl)).execute();
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
            default:
                log.error("Unrecognized update type on update {}", update);
        }
    }

    @Override
    public void onMessageCreate(MessageCreatedUpdate update) {
        Long tamTamUserId = update.getMessage().getSender().getUserId();
        try {
            UserModel userModel = userService.getUser(tamTamUserId);
            if (userModel == null) {
                userService.saveUser(new UserModel(tamTamUserId));
            }
            commandParser.parse(update);
        } catch (APIException | ClientException e) {
            log.error("Can not send response", e);
        }
    }

    @Override
    public void onMessageCallback(MessageCallbackUpdate update) {
        try {
            callbackParser.parse(update);
        } catch (APIException | ClientException e) {
            log.error("Callback error", e);
        }
    }

    @Override
    public void onMessageEdited(MessageEditedUpdate update) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onMessageRemoved(MessageRemovedUpdate update) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onBotAddedToChat(BotAddedToChatUpdate update) {
        try {
            commandParser.help(update.getUser().getUserId());
        } catch (APIException | ClientException e) {
            log.error("Can not send response", e);
        }
    }

    @Override
    public void onBotStarted(BotStartedUpdate update) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onBotRemovedFromChat(BotRemovedFromChatUpdate update) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onUserAddedToChat(UserAddedToChatUpdate userAddedToChatUpdate) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onUserRemovedFromChat(UserRemovedFromChatUpdate userAddedToChatUpdate) {
        log.info("Sorry, operation not support");
    }

    @Override
    public void onChatTitleChanged(ChatTitleChangedUpdate update) {
        log.info("Sorry, operation not support");
    }
}
