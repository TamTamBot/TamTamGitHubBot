package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class GitHubCommitModel {

    private String url;
    private String sha;
    @JsonProperty("html_url")
    private String htmlUrl;
    private Commit commit;
    private String message;
    private GitHubUserModel author;
    private GitHubUserModel committer;
    private List<String> modified;

}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Commit {
    private String url;
    private CommitAuthor author;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class CommitAuthor{
    private String name;
    private String email;
    private String date;
}

