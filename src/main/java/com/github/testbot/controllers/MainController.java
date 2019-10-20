package com.github.testbot.controllers;

import chat.tamtam.botapi.model.Update;
import com.github.testbot.bot.WebhookBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MainController {

    @Autowired
    private WebhookBot webhookBot;

    @PostMapping("/")
    public String receiveBotUpdates(@RequestBody @Valid Update update) {
        webhookBot.handleUpdate(update);
        return "Hello!";
    }
}
