package com.bookstore.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Bad request: ", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Bad Request: " + ex.getMessage()));
    }
}