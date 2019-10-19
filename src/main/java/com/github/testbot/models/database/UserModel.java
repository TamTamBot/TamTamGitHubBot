package com.github.testbot.models.database;

import com.github.testbot.models.github.GitHubRepositoryModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by z0043fkv on 18.10.2019
 */

@Data
@Document(collection = "gitUser")
@NoArgsConstructor
public class UserModel {
    @Id
    private String id;

    @Indexed
    private Long tamTamUserId;

    private GitHubRepositoryModel repository;

    public UserModel(Long tamTamUserId, GitHubRepositoryModel repository) {        this.tamTamUserId = tamTamUserId;
        this.repository = repository;
    }
}
