package com.example.app.config;

import com.example.app.services.IdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.example.app")
public class AppContextConfig {

    @Bean
    public IdProvider idProvider(){
        return new IdProvider();
    }
}
