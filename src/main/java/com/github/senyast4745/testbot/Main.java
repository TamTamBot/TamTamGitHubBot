package com.github.senyast4745.testbot;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.server.BotServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClientException, APIException {


        TamTamBot bot = new TestTamTamBot("http://88e3e9b5.ngrok.io/v1", "RZ-JMfdT2KFuZUTXpSpqzDpK4rbLmYALshvDw86EXug");


        Thread t = new Thread(new BotServer(bot));
        t.start();

    }
}
