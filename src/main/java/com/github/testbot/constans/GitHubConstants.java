package com.github.testbot.constans;


import com.github.testbot.models.github.*;
import com.github.testbot.models.github.GitHubEvents;
import org.jetbrains.annotations.NotNull;

public interface GitHubConstants {

    //See https://developer.github.com/webhooks/#payloads

    String GITHUB_EVENT_NAME_HEADER = "X-GitHub-Event";

    String GITHUB_SIGNATURE_HEADER = "X-Hub-Signature";

    String GITHUB_ID_HEADER = "X-GitHub-Delivery";

    String GITHUB_USER_AGENT_HEADER = "user-agent";

    String GITHUB_USER_AGENT_VALUE = "GitHub-Hookshot";


    //See https://developer.github.com/webhooks/#events

    String GITHUB_EVENT_TYPE_WILDCARD = "*";

    String GITHUB_EVENT_TYPE_COMMIT_COMMENT = "commit_comment";

    String GITHUB_EVENT_TYPE_CREATE = "create";

    String GITHUB_EVENT_TYPE_CHECK_RUN = "check_run";

    String GITHUB_EVENT_TYPE_DELETE = "delete";

    String GITHUB_EVENT_TYPE_PUSH = "push";

    String GITHUB_EVENT_TYPE_PULL_REQUEST = "pull_request";

    String GIT_HUB_ROOT_URL = "https://github.com/";

    String GIT_HUB_ROOT_API_URL = "https://api.github.com/";

    String GIT_HUB_REPOS_URL = "repos/";

    static Class<? extends GitHubEvents> getClass(@NotNull final String githubType) {
        Class<? extends GitHubEvents> event;
        switch (githubType) {
            case GITHUB_EVENT_TYPE_COMMIT_COMMENT:
                event = GitHubCommitCommentEvent.class;
                break;
            case GITHUB_EVENT_TYPE_CREATE:
                event = GitHubCreateEvent.class;
                break;
            case GITHUB_EVENT_TYPE_CHECK_RUN:
                event = GitHubCheckRunEvent.class;
                break;
            case GITHUB_EVENT_TYPE_DELETE:
                event = GitHubDeleteEvent.class;
                break;
            case GITHUB_EVENT_TYPE_PUSH:
                event = GitHubPushEvent.class;
                break;
            case GITHUB_EVENT_TYPE_PULL_REQUEST:
                event = GitHubPullRequestEvent.class;
                break;
            default:
                event = null;
        }

        return event;
    }
}