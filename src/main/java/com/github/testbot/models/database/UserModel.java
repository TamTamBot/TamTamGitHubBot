package com.github.testbot.models.database;

import com.github.testbot.models.github.GitHubRepositoryModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by z0043fkv on 18.10.2019
 */

@Data
@Document(collection = "gitUser")
@NoArgsConstructor
public class UserModel {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique=true)
    private Long tamTamUserId;

    private String githubUserName;

    private String githubPassword;

    private String accessToken;

    private boolean loggedOn = false;

    private Set<GitHubRepositoryModel> githubRepos = new HashSet<>();

    public UserModel(@NotBlank Long tamTamUserId) {
        this.tamTamUserId = tamTamUserId;
    }

    public UserModel(Long tamTamUserId, Set<GitHubRepositoryModel> githubRepos) {
        this.tamTamUserId = tamTamUserId;
        this.githubRepos = githubRepos;
    }

    public UserModel(Long tamTamUserId, String githubUserName, String githubPassword) {
        this.tamTamUserId = tamTamUserId;
        this.githubUserName = githubUserName;
        this.githubPassword = githubPassword;
    }

    public void addRepositoryToSubscriptions(GitHubRepositoryModel gitHubRepositoryModel) {
        this.githubRepos.add(gitHubRepositoryModel);
    }
}
