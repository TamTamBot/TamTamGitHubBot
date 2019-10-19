package com.github.testbot.config;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.testbot.bot.WebhookBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Value("${server.bot.url}")
    private String baseWebhookUrl;

    @Value("${bot.token}")
    private String botToken;

    @Bean(name = "bot")
    public TamTamBotAPI getTamTamBot() {
        return TamTamBotAPI.create(botToken);
    }

    @Bean
    public WebhookBot getWebhookBot() {
        WebhookBot webhookBot = null;
        try {
            webhookBot = new WebhookBot(getTamTamBot());
            webhookBot.setWebhook(baseWebhookUrl + "/tamtam");
        } catch (APIException | ClientException e) {
            e.printStackTrace();
        }
        return webhookBot;
    }
}
