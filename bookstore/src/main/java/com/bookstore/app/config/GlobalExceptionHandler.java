package com.bookstore.app.config;

import com.bookstore.app.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@Order(0)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAllExceptions(Exception ex, ServerWebExchange exchange) {
        log.error("Unexpected error occurred: ", ex);

        String path = exchange.getRequest().getPath().value();

        ErrorResponse response = new ErrorResponse(
                "Internal Server Error: " + ex.getMessage(),
                path,
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        String errorMessage = ex.getAllErrors().get(0).getDefaultMessage();

        ErrorResponse response = new ErrorResponse(
                "Validation Failed: " + errorMessage,
                path,
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBadRequest(IllegalArgumentException ex, ServerWebExchange exchange) {
        log.warn("Bad request: ", ex);

        String path = exchange.getRequest().getPath().value();

        ErrorResponse response = new ErrorResponse(
                "Bad Request: " + ex.getMessage(),
                path,
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }
}
