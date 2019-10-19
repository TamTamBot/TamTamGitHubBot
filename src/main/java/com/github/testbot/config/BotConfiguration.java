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

    @Value("${baseUrl}")
    private String baseWebhookUrl;


    @Bean
    public WebhookBot getWebhookBot() {
        TamTamBotAPI bot = TamTamBotAPI.create(System.getenv("TOKEN"));
        WebhookBot webhookBot = null;
        try {
            webhookBot = new WebhookBot(bot);
            webhookBot.setWebhook(baseWebhookUrl);
        } catch (APIException | ClientException e) {
            e.printStackTrace();
        }
        return webhookBot;
    }
}
