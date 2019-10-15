package com.github.senyast4745.testbot.server;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senyast4745.testbot.bot.TamTamBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;
import static spark.Spark.post;

public class BotServer implements Runnable {

    private TamTamBot bot;
    public BotServer(TamTamBot bot) {
        this.bot = bot;
    }

    private final Logger log = LoggerFactory.getLogger(BotServer.class);

    @Override
    public void run() {
        port(8080);
        post("/v1", (request, resp) -> {
            log.info("I am here");
            JacksonSerializer serializer = new JacksonSerializer();
            Update update = serializer.deserialize(request.body(), Update.class);
            assert update != null;
            handleUpdate(update);
            log.info(request.body());
            return "";
        });
    }

    private void handleUpdate(Update update){
        switch (update.getType()) {
            case (Update.BOT_ADDED):
                bot.onBotAddedToChat((BotAddedToChatUpdate) update);
                break;
            case (Update.BOT_STARTED):
                bot.onBotStarted((BotStartedUpdate) update);
                break;
            case (Update.BOT_REMOVED):
                bot.onBotRemovedFromChat((BotRemovedFromChatUpdate) update);
                break;
            case (Update.MESSAGE_CALLBACK):
                bot.onMessageCallback((MessageCallbackUpdate) update);
                break;
            case (Update.MESSAGE_CREATED):
                bot.onMessageCrete((MessageCreatedUpdate) update);
                break;
            case (Update.MESSAGE_EDITED):
                bot.onMessageEdited((MessageEditedUpdate) update);
                break;
            case (Update.MESSAGE_REMOVED):
                bot.onMessageRemoved((MessageRemovedUpdate) update);
                break;
            case (Update.USER_ADDED):
                bot.onUserAddedToChat((UserAddedToChatUpdate) update);
                break;
            case (Update.USER_REMOVED):
                bot.onUserRemovedFromChat((UserRemovedFromChatUpdate) update);
                break;
            case (Update.CHAT_TITLE_CHANGED):
                bot.onChatTitleChanged((ChatTitleChangedUpdate) update);
        }
    }
}
