package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class GitHubCommitCommentEvent implements CanSandedToSubscriber {
    private String action;
    private GitHubCommentModel comment;
    private GitHubRepositoryModel repository;
    private GitHubUserModel owner;

    @Override
    public String toString() {
        return "Comment to commit " + comment.getHtmlUrl() + " " +
                action + "\n\rComment: \"" + comment.getBody() + "\"\n\rBy user " +
                comment.getUser().getLogin() + "\n\rTo repository " + repository.getFullName();
    }
}
