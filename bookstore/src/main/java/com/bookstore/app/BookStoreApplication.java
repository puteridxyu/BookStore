package com.bookstore.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BookStoreApplication {

    @Value("${spring.r2dbc.url:NOT_SET}")
    private String r2dbcUrl;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @PostConstruct
    public void logR2dbcUrl() {
        System.out.println("  Loaded spring.r2dbc.url = " + r2dbcUrl);
    }
}


