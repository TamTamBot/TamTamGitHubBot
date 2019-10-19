package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class GitHubPullRequestModel {

    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("diff_url")
    private String diffUrl;
    @JsonProperty("patch_url")
    private String patchUrl;
    @JsonProperty("issue_url")
    private String issueUrl;
    @JsonProperty("commits_url")
    private String commitsUrl;
    @JsonProperty("review_comments_url")
    private String reviewCommentsUrl;
    @JsonProperty("comments_url")
    private String commentsUrl;
    @JsonProperty("statuses_url")
    private String statusesUrl;
    private String state;
    private Long number;
    private Boolean locked;
    private String title;
    private GitHubUserModel user;
    private String body;
    private List<Label> labels;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("closed_at")
    private String closedAt;
    @JsonProperty("merged_at")
    private String mergedAt;
    private GitHubUserModel assignee;
    private List<GitHubUserModel> assignees;
    @JsonProperty("requested_reviewers")
    private List<GitHubUserModel> requestedReviewers;
    @JsonProperty("requested_teams")
    private List<RequestTeam> requestedTeams;
    private GitHubCommitModel head;
    @JsonProperty("_links")
    private Links links;
    private boolean merged;
    private boolean mergeable;
    private boolean rebaseable;
    @JsonProperty("mergeable_state")
    private String mergeableState;
    @JsonProperty("merged_by")
    private GitHubUserModel merged_by;
    private int comments;
    @JsonProperty("review_comments")
    private int reviewComments;
    private int commits;
    private int additions;
    private int deletions;
    @JsonProperty("changed_files")
    private int changedFiles;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    private static final class Links {
        private Link self;
        private Link html;
        private Link issue;
        @JsonProperty("review_comments")
        private Link reviewComments;
        private Link commits;
        private Link statuses;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    private static final class Link {
        private String href;
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class Label {
    private String url;
    private String name;
    private String description;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class SimpleCommit {
    private String label;
    private String ref;
    private String sha;
    private GitHubUserModel user;
    private GitHubRepositoryModel repo;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class RequestTeam {
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String name;
    private String slug;
    private String description;
    @JsonProperty("repositories_url")
    private String repositoriesUrl;
}





