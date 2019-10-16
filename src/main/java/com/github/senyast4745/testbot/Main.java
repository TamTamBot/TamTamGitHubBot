package com.github.senyast4745.testbot;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.repository.UsersRepository;
import com.github.senyast4745.testbot.server.BotServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static FileInputStream fis;
    private static Properties properties = new Properties();

    public static void main(String[] args) throws ClientException, APIException, IOException {

        List<String> serverProperties = getProperty("server.bot.url", "bot.token");
        TamTamBot bot = new TestTamTamBot(serverProperties.get(0), serverProperties.get(1));

        UsersRepository.init();
        Thread t = new Thread(new BotServer(bot));
        t.start();

    }

    public static List<String> getProperty(String ... key) throws IOException {

        fis = new FileInputStream("src/main/resources/application.properties");
        properties.load(fis);

        List<String>  propertiesList = new ArrayList<>();
        Arrays.asList(key).forEach(k -> propertiesList.add(properties.getProperty(k, "default")));
        return propertiesList;

    }



}
