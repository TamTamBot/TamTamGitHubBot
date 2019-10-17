package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPushEvent implements CanSandedToSubscriber {
    private boolean force;
    private List<GitHubCommitModel> commits;
    private GitHubRepositoryModel repository;
    private PushAuthor pusher;
    private GitHubUserModel sender;

    @Getter
    @Setter
    private static
    class PushAuthor {
        private String name;
        private String email;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("Push to ").append(repository.getFullName()).append( "link: ")
                .append(repository.getHtmlUrl()).append("\n\r Commits:\n\r");

        commits.forEach(c -> toString.append(" message: ").append(c.getMessage()).append("\n\r"));
        toString.append("Pusher name: ").append(pusher.getName());
        return toString.toString();
    }
}



