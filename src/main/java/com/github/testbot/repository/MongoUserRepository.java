package com.github.testbot.repository;

import com.github.testbot.models.database.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by z0043fkv on 18.10.2019
 */

public interface MongoUserRepository extends MongoRepository<UserModel, String> {

    public List<UserModel> findAllByTamTamUserId(Long id);

    public List<UserModel> findAllByRepository_FullName(String fullName); //Need to fix
}
