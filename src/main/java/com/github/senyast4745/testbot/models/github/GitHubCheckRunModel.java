package com.github.senyast4745.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@SuppressWarnings("ALL")
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCheckRunModel {

    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String status;
    private String conclusion;
    @JsonProperty("started_at")
    private String startedAt;
    @JsonProperty("completed_at")
    private String completedAt;
    private Output output;
    private String name;
    @JsonProperty("check_suite")
    private CheckSuite checkSuite;
    private GitHubAppModel app;
    @JsonProperty("pull_requests")
    private List<SimplePullRequest> pullRequests;



    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Output {
        private String title;
        private String summary;
        private String text;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class CheckSuite {
        private String status;
        private String conclusion;
        private String url;
        @JsonProperty("pull_requests")
        private List<SimplePullRequest> pullRequests;
        private GitHubAppModel app;

    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class SimplePullRequest {
        private String url;
        private SimpleBranch head;
        private SimpleBranch base;

        @Getter
        @Setter
        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class SimpleBranch{
            private String ref;
            private SimpleRepo repo;

            @Getter
            @Setter
            @ToString
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static final class SimpleRepo{
                private String url;
                private String name;
            }
        }
    }

}
