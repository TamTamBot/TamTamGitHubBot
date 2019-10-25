package com.github.testbot.repositories;

import com.github.testbot.models.database.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by z0043fkv on 18.10.2019
 */

public interface UserRepository extends MongoRepository<UserModel, String> {

    UserModel findOneByTamTamUserId(Long id);

    List<UserModel> findAllByTamTamUserId(Long id);

    List<UserModel> findAllByGithubReposFullName(String fullName);
}
