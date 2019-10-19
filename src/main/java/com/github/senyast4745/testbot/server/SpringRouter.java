package com.github.senyast4745.testbot.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Created by z0043fkv on 18.10.2019
 */

@Configuration
public class SpringRouter {

    @Bean
    public RouterFunction userRoutes(UserHandler userHandler) {

        return RouterFunctions
                .route(RequestPredicates.POST("/users").and(accept(MediaType.APPLICATION_JSON)), userHandler::addUser)
                .andRoute(RequestPredicates.GET("/users").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUsers)
                .andRoute(RequestPredicates.GET("/users/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUserById);
    }
}