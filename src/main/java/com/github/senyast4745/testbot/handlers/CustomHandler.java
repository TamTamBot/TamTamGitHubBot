package com.github.senyast4745.testbot.handlers;

import okhttp3.Request;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * Created by z0043fkv on 18.10.2019
 */
@FunctionalInterface
public interface CustomHandler {
    public void handle(ServerRequest request);
}
