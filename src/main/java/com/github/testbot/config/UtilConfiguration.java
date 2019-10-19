package com.github.testbot.config;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfiguration {

    @Bean(name = "serializer")
    public JacksonSerializer getSerializer(){
       return new JacksonSerializer();
    }
}
