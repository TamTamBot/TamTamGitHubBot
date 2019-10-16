package com.github.senyast4745.testbot;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.repos.GitHubUser;
import com.github.senyast4745.testbot.server.BotServer;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static FileInputStream fis;
    private static Properties properties = new Properties();

    public static void main(String[] args) throws ClientException, APIException, IOException {

        TamTamBot bot = new TestTamTamBot(getProperty("server.bot.url"), getProperty("bot.token"));

        GitHubUser.init();
        Thread t = new Thread(new BotServer(bot));
        t.start();

    }

    private static String getProperty(String key) throws IOException {

        fis = new FileInputStream("src/main/resources/application.properties");
        properties.load(fis);
        return  properties.getProperty(key);

    }



}
