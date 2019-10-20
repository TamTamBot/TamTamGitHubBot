package com.github.testbot.interfaces;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.Update;
import org.jetbrains.annotations.NotNull;

public interface Parser {
    <T extends Update> void parse(@NotNull T update) throws APIException, ClientException;
}
