package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitComment;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GitHubCommentModel {
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    private GitHubUserModel user;
    @JsonProperty("created_at")
    private String createdAt;
    private String body;
}
