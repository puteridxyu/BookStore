package com.bookstore.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class BookStoreApplication {

    private static final Logger log = LoggerFactory.getLogger(BookStoreApplication.class);

    @Value("${spring.r2dbc.url:NOT_SET}")
    private String r2dbcUrl;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @PostConstruct
    public void logR2dbcUrl() {
        log.info("Loaded spring.r2dbc.url = {}", r2dbcUrl);
    }
}


