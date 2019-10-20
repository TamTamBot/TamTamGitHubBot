package com.github.testbot.github;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.exceptions.SerializationException;
import chat.tamtam.botapi.model.NewMessageBody;
import com.github.testbot.interfaces.GitHubActions;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.*;
import com.github.testbot.repository.MongoUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.testbot.constans.GitHubConstants.*;

@Slf4j
@Component
public class WebhookGitHub implements GitHubActions {

    private final TamTamBotAPI bot;

    private final MongoUserRepository usersRepository;

    private final JacksonSerializer serializer;


    public WebhookGitHub(TamTamBotAPI bot, MongoUserRepository usersRepository, JacksonSerializer serializer) {
        this.bot = bot;
        this.usersRepository = usersRepository;
        this.serializer = serializer;
    }

    @Override
    public void handleEvent(String body, String eventType) {
        try {
            switch (eventType) {
                case GITHUB_EVENT_TYPE_CREATE:
                    GitHubCreateEvent createEvent = serializer.deserialize(body, GitHubCreateEvent.class);
                    defaultEvent(createEvent);
                    break;
                case GITHUB_EVENT_TYPE_PUSH:
                    GitHubPushEvent pushEvent = serializer.deserialize(body, GitHubPushEvent.class);
                    defaultEvent(pushEvent);
                    break;
                case GITHUB_EVENT_TYPE_COMMIT_COMMENT:
                    GitHubCommitCommentEvent commitCommentEvent = serializer.deserialize(body, GitHubCommitCommentEvent.class);
                    defaultEvent(commitCommentEvent);
                    break;
                case GITHUB_EVENT_TYPE_PULL_REQUEST:
                    GitHubPullRequestEvent pullRequestEvent = serializer.deserialize(body, GitHubPullRequestEvent.class);
                    defaultEvent(pullRequestEvent);
                    break;
                default:
                    log.info("Unknown command " + eventType);
            }
        } catch (SerializationException e) {
            log.error("Can not deserealize " + body, e);
        }
    }

    @Override
    public void defaultEvent(GitHubEvents events) {
        sendMessageToUsers(events);
    }

    @Override
    public void sendMessageToUsers(GitHubEvents event) {
        NewMessageBody body = new NewMessageBody(event.toString(), null, null);
        List<UserModel> users = usersRepository.findAllByRepository_FullName(event.getRepository().getFullName());
        users.forEach(user -> {
            try {
                bot.sendMessage(body).userId(user.getTamTamUserId()).execute();
            } catch (APIException | ClientException e) {
                log.error("Error while sending GitHub event to user", e);
            }
        });
    }
}
