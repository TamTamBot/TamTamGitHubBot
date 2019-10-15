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
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.server.Server;
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


        TamTamBot bot = new TestTamTamBot("http://88e3e9b5.ngrok.io/v1", "RZ-JMfdT2KFuZUTXpSpqzDpK4rbLmYALshvDw86EXug");


        Thread t = new Thread(new Server(bot));
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
