package com.github.senyast4745.testbot;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import com.github.senyast4745.testbot.bot.TamTamBot;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.repository.UsersRepository;
import com.github.senyast4745.testbot.server.BotServer;
import org.kohsuke.github.GHAuthorization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttp3Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static FileInputStream fis;
    private static Properties properties = new Properties();

    public static void main(String[] args) throws ClientException, APIException, IOException, ExecutionException, InterruptedException {


        UsersRepository.init();
/*        Thread t = new Thread(new BotServer(bot));
        t.start();*/

        SpringApplication.run(Main.class, args);

    }

    public static List<String> getProperty(String ... key) throws IOException {


        try {
            fis = new FileInputStream("cofig/application-" + System.getProperty("act.profile") + ".properties");
        } catch (IOException e){
            fis = new FileInputStream("src/main/resources/application.properties");
        }
        properties.load(fis);

        List<String>  propertiesList = new ArrayList<>();

        Arrays.asList(key).forEach(k -> propertiesList.add(properties.getProperty(k, "default")));
        return propertiesList;

    }




}
