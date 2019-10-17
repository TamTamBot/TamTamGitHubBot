package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubCreateEvent implements CanSandedToSubscriber {
    private String ref;
    @JsonProperty("ref_type")
    private String refType;
    @JsonProperty("master_branch")
    private String masterBranch;
    private String description;
    @JsonProperty("pusher_type")
    private String pusherType;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;
}
