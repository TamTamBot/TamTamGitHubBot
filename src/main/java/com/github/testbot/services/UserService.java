package com.github.testbot.services;


import com.github.testbot.models.database.UserModel;
import com.github.testbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUser(Long tamTamUserId) {
        return userRepository.findOneByTamTamUserId(tamTamUserId);
    }

    public void  saveUser(UserModel userModel) {
        userRepository.save(userModel);
    }

    public List<UserModel> getUsersByTamTamUserId(Long tamTamUserId) {
        return userRepository.findAllByTamTamUserId(tamTamUserId);
    }

    public List<UserModel> getUsersByGithubRepoName(String repoName) {
        return userRepository.findAllByGithubReposFullName(repoName);
    }

}
