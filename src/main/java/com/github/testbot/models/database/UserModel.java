package com.github.testbot.models.database;

import com.github.testbot.models.github.GitHubRepositoryModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

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

    private boolean loggedOn = false;

    private GitHubRepositoryModel githubRepo;

    public UserModel(@NotBlank Long tamTamUserId) {
        this.tamTamUserId = tamTamUserId;
    }

    public UserModel(Long tamTamUserId, GitHubRepositoryModel githubRepo) {
        this.tamTamUserId = tamTamUserId;
        this.githubRepo = githubRepo;
    }

    public UserModel(Long tamTamUserId, String githubUserName, String githubPassword) {
        this.tamTamUserId = tamTamUserId;
        this.githubUserName = githubUserName;
        this.githubPassword = githubPassword;
    }
}
