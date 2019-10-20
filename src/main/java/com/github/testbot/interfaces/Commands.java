package com.github.testbot.interfaces;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;

public interface Commands {
    void help(long senderId) throws ClientException, APIException;
    void subscribeToRepo(long senderId) throws ClientException, APIException;
    void list(long senderId) throws APIException, ClientException;
    void sendSimpleMessage(long senderId, String message) throws ClientException, APIException;
}
