package com.github.testbot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.Update;
import com.github.testbot.api.impl.WebhookBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public class Main {

    public static void main(String[] args) throws ClientException, APIException {

        Logger logger = LoggerFactory.getLogger(Main.class);

        String tempUrl = "http://1db56370.ngrok.io";
        TamTamBotAPI bot = TamTamBotAPI.create(System.getenv("TOKEN"));
        WebhookBot webhookBot = new WebhookBot(bot);
        webhookBot.setWebhook(tempUrl);

        Service server = Service.ignite();
        server.port(8080);
        server.post("/", (request, resp) -> {
            logger.info("I am here");
            JacksonSerializer serializer = new JacksonSerializer();
            Update update = serializer.deserialize(request.body(), Update.class);
            if (update != null) {
                webhookBot.handleUpdate(update);
            }
            return "Hello";
        });
    }
}
