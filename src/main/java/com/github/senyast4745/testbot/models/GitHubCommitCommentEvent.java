package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GitHubCommitCommentEvent {
    private String action;
    private GitHubCommentModel comment;
    private GitHubRepositoryModel repository;
    private GitHubUserModel owner;

}
