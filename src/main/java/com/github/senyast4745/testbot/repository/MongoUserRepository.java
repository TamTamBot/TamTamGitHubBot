package com.github.senyast4745.testbot.repository;

import com.github.senyast4745.testbot.models.database.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by z0043fkv on 18.10.2019
 */
@Repository
public interface MongoUserRepository extends MongoRepository<UserModel, String > {
    UserModel findByTamTamUserId(Long id);
    UserModel findByRepository_FullName(String fullName);
}
