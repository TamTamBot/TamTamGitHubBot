package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCheckRunEvent implements CanSandedToSubscriber {
    private String action;
    @JsonProperty("check_run")
    private GitHubCheckRunModel checkRun;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;
}
