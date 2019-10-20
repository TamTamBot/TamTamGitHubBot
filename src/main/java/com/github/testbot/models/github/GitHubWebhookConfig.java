package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubWebhookConfig {
    private String url;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("insecure_ssl")
    private String ssl;

    public GitHubWebhookConfig(String url, String contentType, String ssl) {
        this.url = url;
        this.contentType = contentType;
        this.ssl = ssl;
    }
}
