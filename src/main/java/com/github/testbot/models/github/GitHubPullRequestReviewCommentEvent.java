package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPullRequestReviewCommentEvent implements GitHubEvents {

    private String action;
    private GitHubCommentModel comment;
    @JsonProperty("pull_request")
    private GitHubPullRequestModel pullRequest;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;

    @Override
    public String toString() {
        return "Comment to your pull request " + pullRequest.getBody() + " " +
                action + "\n\rComment: \"" + comment.getBody() + "\"\n\rBy user " +
                comment.getUser().getLogin() + "\n\rTo repository " + repository.getFullName() + ".\n\r" +
                "Comment url: " + comment.getHtmlUrl();
    }
}
