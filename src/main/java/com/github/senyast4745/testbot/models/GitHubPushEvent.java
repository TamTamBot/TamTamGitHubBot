package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPushEvent {
    private boolean force;
    private List<GitHubCommitModel> commits;
    private GitHubUserModel sender;
}

@Getter
@Setter
class PushAuthor{
    private String name;
    private String email;
}

