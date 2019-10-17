package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubDeleteEvent implements GitHubEvents {
    private String ref;
    @JsonProperty("ref_type")
    private String refType;
    @JsonProperty("pusher_type")
    private String pusherType;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;
}
