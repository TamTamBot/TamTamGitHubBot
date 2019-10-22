package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPullRequestEvent implements GitHubEvents {

    /**
     * Action states.
     * assigned,unassigned,review_requested,
     * review_request_removed, labeled, unlabeled, opened, edited,
     * closed, ready_for_review, locked, unlocked, or reopened.
     */

    private String action;
    private Integer number;
    @JsonProperty("pull_request")
    private GitHubPullRequestModel pullRequest;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;

    @Override
    public String toString() {
        return "Pull request\n\rAction " + action + "\n\rPull request " + pullRequest.getHtmlUrl() + "\n\rwith title: " +
                pullRequest.getTitle() + "\n\rand comment: " + pullRequest.getBody() + "\n\rBy user: "
                + sender.getLogin() + "\n\rTo repository: " + repository.getFullName();
    }
}
