package com.github.testbot.interfaces;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.GitHubRepositoryModel;

public interface Commands {
    void help(long senderId) throws ClientException, APIException;
    void subscribeToRepo(long senderId, UserModel user, GitHubRepositoryModel repository) throws ClientException, APIException;
    void list(long senderId) throws APIException, ClientException;
    void sendSimpleMessage(long senderId, String message) throws ClientException, APIException;
}
