package com.github.senyast4745.testbot;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.TamTamClient;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.client.impl.OkHttpTransportClient;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import chat.tamtam.botapi.queries.AnswerOnCallbackQuery;
import chat.tamtam.botapi.queries.GetChatsQuery;
import chat.tamtam.botapi.queries.GetUploadUrlQuery;
import chat.tamtam.botapi.queries.SendMessageQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senyast4745.testbot.api.AbstractLongPollBotAPI;
import com.github.senyast4745.testbot.api.impl.LongPollBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.events.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static spark.Spark.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClientException, APIException {


        TamTamBotAPI bot = TamTamBotAPI.create("-7LYsD1UEzSRp1BAMeaqBDuz1eMf98GzFwxnGScLmp0");
        bot.subscribe(new SubscriptionRequestBody("http://f52f7323.ngrok.io/v1")).execute();


        Thread t = new Thread(() -> {
            port(8080);
            post("/v1", (request, resp) -> {
                log.info("I am here");
                JacksonSerializer serializer = new JacksonSerializer();
                Update update = serializer.deserialize(request.body(), Update.class);
                ObjectMapper mapper = new ObjectMapper();
//                Update msg = mapper.readValue(request.body(), Update.class);
                log.info(request.body());
                return "Hello";
            });
        });
        t.start();


//        Thread t2 = new Thread(() -> {
//            AbstractLongPollBotAPI api = null;
//            try {
//                api = new LongPollBot(bot);
//                api.start();
//            } catch (APIException | ClientException e) {
//                e.printStackTrace();
//            }
//        });
//        t2.start();;

    }
}
