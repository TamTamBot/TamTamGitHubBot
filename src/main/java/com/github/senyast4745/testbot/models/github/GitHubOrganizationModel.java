package com.github.senyast4745.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubOrganizationModel {
    private String login;
    private String url;
    @JsonProperty("repos_url")
    private String reposUrl;
    @JsonProperty("events_url")
    private String eventsUrl;
    @JsonProperty("hooks_url")
    private String hooksUrl;
    @JsonProperty("issues_url")
    private String issuesUrl;
    @JsonProperty("members_url")
    private String membersUrl;
    private String description;
}
