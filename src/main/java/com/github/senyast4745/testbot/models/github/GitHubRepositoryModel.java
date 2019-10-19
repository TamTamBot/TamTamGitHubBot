package com.github.senyast4745.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class GitHubRepositoryModel {
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private GitHubUserModel owner;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String url;

}


