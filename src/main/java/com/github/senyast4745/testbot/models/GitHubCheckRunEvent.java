package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCheckRunEvent implements GitHubEvents {
    private String action;
    @JsonProperty("check_run")
    private GitHubCheckRunModel checkRun;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;

    @Override
    public String toString() {
        return "Check run event action " + action + " " + checkRun.getCheckSuite().toString() + "\n\rBy user " +
                sender.getLogin() + "\n\rIn repository " + repository.getFullName();
    }
}
