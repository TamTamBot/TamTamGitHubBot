package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCreateWebhook {
    private String name;
    private boolean active;
    private List<String> events;
    private GitHubWebhookConfig config;

    public GitHubCreateWebhook(String name, boolean active, List<String> events, GitHubWebhookConfig config) {
        this.name = name;
        this.active = active;
        this.events = events;
        this.config = config;
    }
}


