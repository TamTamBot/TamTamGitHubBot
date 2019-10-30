package com.github.testbot.controllers;

import chat.tamtam.botapi.model.Update;
import com.github.testbot.bot.WebhookBot;
import com.github.testbot.constans.GitHubConstants;
import com.github.testbot.github.WebhookGitHub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class MainController {

    private final WebhookBot webhookBot;

    private final WebhookGitHub webhookGitHub;

    public MainController(WebhookBot webhookBot, WebhookGitHub webhookGitHub) {
        this.webhookBot = webhookBot;
        this.webhookGitHub = webhookGitHub;
    }

    @GetMapping("/healthcheck")
    public String healthcheckEndpoint() {
        return "OK!";
    }

    @PostMapping("/tamtam")
    public ResponseEntity<Void> receiveBotUpdates(@RequestBody @Valid final Update update) {
        webhookBot.handleUpdate(update);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/github")
    public ResponseEntity<Void> receiveGitHubUpdates(@RequestBody @Valid final String body,
                                                     @RequestHeader(GitHubConstants.GITHUB_EVENT_NAME_HEADER)
                                                             String header) {
        log.info("New update from github\uD83C\uDF44");
        if (header == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            webhookGitHub.handleEvent(body, header);
        } catch (IllegalArgumentException e) {
            log.error("Incorrect input data {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unknown error while handle event");
        }
        return ResponseEntity.ok().build();
    }
}
