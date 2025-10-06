package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.service.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private EventJoinObserver eventJoinObserver;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Bean
    public EventSubject eventSubject() {
        EventSubject subject = new EventSubject();
        subject.addObserver(eventJoinObserver);
        return subject;
    }
}