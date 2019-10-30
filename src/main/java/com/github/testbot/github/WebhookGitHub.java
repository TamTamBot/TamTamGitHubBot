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
import com.github.testbot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static com.github.testbot.constans.GitHubConstants.*;

@Slf4j
@Component
public class WebhookGitHub implements GitHubActions {

    private UserService userService;

    private final TamTamBotAPI bot;

    private final JacksonSerializer serializer;


    public WebhookGitHub(TamTamBotAPI bot, UserService userService, JacksonSerializer serializer) {
        this.bot = bot;
        this.userService = userService;
        this.serializer = serializer;
    }

    @Override
    public void handleEvent(final String body, @NotNull final String eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type can not be null.");
        }
        try {
            switch (eventType) {
                case GITHUB_EVENT_TYPE_CREATE:
                    GitHubCreateEvent createEvent = serializer.deserialize(body, GitHubCreateEvent.class);
                    defaultEvent(Optional.ofNullable(createEvent));
                    break;
                case GITHUB_EVENT_TYPE_PUSH:
                    GitHubPushEvent pushEvent = serializer.deserialize(body, GitHubPushEvent.class);
                    defaultEvent(Optional.ofNullable(pushEvent));
                    break;
                case GITHUB_EVENT_TYPE_COMMIT_COMMENT:
                    GitHubCommitCommentEvent commitCommentEvent =
                            serializer.deserialize(body, GitHubCommitCommentEvent.class);
                    defaultEvent(Optional.ofNullable(commitCommentEvent));
                    break;
                case GITHUB_EVENT_TYPE_PULL_REQUEST:
                    GitHubPullRequestEvent pullRequestEvent = serializer
                            .deserialize(body, GitHubPullRequestEvent.class);
                    defaultEvent(Optional.ofNullable(pullRequestEvent));
                    break;
                case GITHUB_EVENT_TYPE_PULL_REQUEST_REVIEW:
                    GitHubPullRequestReviewEvent pullRequestReviewEvent =
                            serializer.deserialize(body, GitHubPullRequestReviewEvent.class);
                    defaultEvent(Optional.ofNullable(pullRequestReviewEvent));
                    break;
                case GITHUB_EVENT_TYPE_PULL_REQUEST_REVIEW_COMMENT:
                    GitHubPullRequestReviewCommentEvent pullRequestReviewCommentEvent =
                            serializer.deserialize(body, GitHubPullRequestReviewCommentEvent.class);
                    defaultEvent(Optional.ofNullable(pullRequestReviewCommentEvent));
                    break;
                default:
                    log.info("Unknown command " + eventType);
            }
        } catch (SerializationException e) {
            log.error("Can not deserealize " + body, e);
        }
    }

    @Override
    public void defaultEvent(@NotNull final Optional<GitHubEvents> optEvent) {
        GitHubEvents events = optEvent.orElseThrow(() -> new IllegalArgumentException("Event can not be null."));
        List<UserModel> subscriptions = userService.getUsersByGithubRepoName(events.getRepository().getFullName());
        sendMessageToUsers(events, subscriptions);
    }

    @Override
    public void sendMessageToUsers(GitHubEvents event, List<UserModel> users) {
        NewMessageBody body = new NewMessageBody(event.toString(), null, null);
        users.forEach(user -> {
            try {
                bot.sendMessage(body).userId(user.getTamTamUserId()).execute();
            } catch (APIException | ClientException e) {
                log.error("Error while sending GitHub event to user", e);
            }
        });
    }
}
