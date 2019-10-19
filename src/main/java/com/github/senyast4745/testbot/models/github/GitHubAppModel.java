package com.github.senyast4745.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubAppModel {

    private String slug;
    private GitHubOrganizationModel owner;
    private String name;
    private String description;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
//    private Permission permission;
    private List<String> events;
/*
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Permission{
        private String metadata;
        private String contents;
        private String issues;
        @JsonProperty("single_file")
        private String singleFile;
    }*/


}
