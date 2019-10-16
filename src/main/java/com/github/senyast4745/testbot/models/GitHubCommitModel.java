package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubCommitModel {
    private String url;
    private String sha;
    @JsonProperty("html_url")
    private String htmlUrl;
    private Commit commit;
    private String message;
    private GitHubUserModel author;
    private GitHubUserModel committer;

}

@Getter
@Setter
class Commit {
    private String url;
    private CommitAuthor author;
}

@Getter
@Setter
class CommitAuthor{
    private String name;
    private String email;
    private String date;
}

