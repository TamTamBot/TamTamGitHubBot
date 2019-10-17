package com.github.senyast4745.testbot.server;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.SerializationException;
import chat.tamtam.botapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senyast4745.testbot.bot.NotifyClass;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.NotifyClassImpl;
import com.github.senyast4745.testbot.constans.GitHubConstants;
import com.github.senyast4745.testbot.constans.GitHubEvents;
import com.github.senyast4745.testbot.models.GitHubCommitCommentEvent;
import com.github.senyast4745.testbot.models.GitHubPushEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;
import static spark.Spark.post;

public class BotServer implements Runnable {

    private TamTamBot bot;
    private NotifyClass notifyClass;
    public BotServer(TamTamBot bot) {
        this.bot = bot;
        notifyClass = new NotifyClassImpl();
    }

    private final Logger log = LoggerFactory.getLogger(BotServer.class);

    private ObjectMapper mapper = new ObjectMapper();

    private JacksonSerializer serializer = new JacksonSerializer();

    @Override
    public void run() {
        port(8080);
        post("/v1", (request, resp) -> {
            log.info("I am here");

            Update update = serializer.deserialize(request.body(), Update.class);
            assert update != null;
            handleUpdate(update);
            log.info(request.body());
            return "";
        });
        post("github", (request, response) ->{
            String header = request.headers(GitHubConstants.GITHUB_EVENT_NAME_HEADER);
            log.info("Github event " + header);
            handleGitHub(header, request.body());
            if(header.equals("commit_comment")){
                GitHubCommitCommentEvent commentEvent = serializer.deserialize(request.body(), GitHubCommitCommentEvent.class);
                log.info("Commit comment " + commentEvent.toString());
                notifyClass.onCommitComment(commentEvent);
            }
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

    private void handleGitHub(String event, String requestBody) throws SerializationException {
        GitHubEvents events = GitHubEvents.DEFAULT;
        try{
            events = GitHubEvents.valueOf(event);
        } catch (IllegalArgumentException ignore){
        }

        switch (events){
            case PUSH:
                GitHubPushEvent pushEvent = serializer.deserialize(requestBody, GitHubPushEvent.class);
                break;
        }

    }
}