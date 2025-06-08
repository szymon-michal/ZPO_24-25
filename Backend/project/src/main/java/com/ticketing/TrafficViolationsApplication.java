package com.ticketing;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TrafficViolationsApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Start Spring Boot application
        context = SpringApplication.run(TrafficViolationsApplication.class, args);

    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}