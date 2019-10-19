package com.github.senyast4745.testbot.configuration;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by z0043fkv on 18.10.2019
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    public String getDatabaseName() {
        return "testBot";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("127.0.0.1");
    }
}