package com.github.testbot.interfaces;

import com.github.testbot.models.github.*;

public interface GitHubActions {

    void handleEvent(String body, String eventType);

    void defaultEvent(GitHubEvents events);

    default void onCommitComment(GitHubCommitCommentEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }
    default void onCreate(GitHubCreateEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }
    default void onCheckRun(GitHubCheckRunEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }
    default void onDelete(GitHubDeleteEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }
    default void onPullRequest(GitHubPullRequestEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }
    default void onPush(GitHubPushEvent event){
        throw new UnsupportedOperationException("Sorry, operation not support");
    }

    void sendMessageToUsers(GitHubEvents event);

}
