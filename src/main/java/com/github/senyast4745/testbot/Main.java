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
import com.github.senyast4745.testbot.api.AbstractLongPollBotAPI;
import com.github.senyast4745.testbot.api.impl.LongPollBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.events.EventListener;

import java.util.concurrent.Future;

import static spark.Spark.*;

public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClientException, APIException {


        TamTamBotAPI bot = TamTamBotAPI.create( "RZ-JMfdT2KFuZUTXpSpqzDpK4rbLmYALshvDw86EXug");
        NewMessageBody body = new NewMessageBody(" Привет, мир! ", null, null);
        GetChatsQuery query = bot.getChats();

        SendMessageQuery sendMessageQuery = bot.sendMessage(body).chatId(query.execute().getChats().get(0).getChatId());



        Thread t = new Thread(() -> {
            port(8080);
            post("/v1", (request, resp) -> {
                log.info("I am here");
                return "Hello";
            });
        });
        t.start();

//        bot.subscribe(new SubscriptionRequestBody("https://botapi.tamtam.chat/messages"));
        bot.subscribe(new SubscriptionRequestBody("http://0.0.0.0:8080/v1"));

        /*Thread t2 = new Thread(() -> {
            AbstractLongPollBotAPI api = null;
            try {
                api = new LongPollBot(bot);
                api.start();
            } catch (APIException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        });
        t2.start();;*/

// Синхронизация
//        SendMessageResult result = sendMessageQuery.execute();

          /*  Long marker = null;
            bot.getUpdates().marker(null).execute();
            while (true) {
                UpdateList updateList = bot.getUpdates().marker(marker).execute();
                marker = updateList.getMarker();
                updateList.getUpdates().forEach(update -> {
//                    System.out.println(update);
                    System.out.println(((MessageCreatedUpdate) update).getMessage().getBody());

                });
            }
*/

/*        while (true) {

            bot.getMessages().execute().getMessages().forEach(m -> {
                System.out.println(m.toString());
            });
        }*/

// Async
//        Future<SendMessageResult> futureResult = sendMessageQuery.enqueue();
//        BotStartedUpdate update = new BotStartedUpdate();
    }
}
