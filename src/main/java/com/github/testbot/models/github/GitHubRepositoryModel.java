package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GitHubRepositoryModel {

    @JsonProperty("full_name")
    private String fullName;

    private String name;

    private GitHubUserModel owner;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String url;

    @EqualsAndHashCode.Exclude
    private Long webhookId;

    @EqualsAndHashCode.Exclude
    private String accessToken;
}


