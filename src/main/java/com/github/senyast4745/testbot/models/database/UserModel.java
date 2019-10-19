package com.github.senyast4745.testbot.models.database;

import com.github.senyast4745.testbot.models.github.GitHubOrganizationModel;
import com.github.senyast4745.testbot.models.github.GitHubRepositoryModel;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by z0043fkv on 18.10.2019
 */

@Data
@Document(collection = "gitUser")
public class UserModel {
    @Id
    private ObjectId id;

    @Indexed
    private Long tamTamUserId;

    private GitHubRepositoryModel repository;

}
