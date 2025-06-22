package com.bookstore.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements WebFilter {

    private static final String[] EXCLUDED_PATHS = {
            "/v3/api-docs", "/swagger", "/swagger-ui.html", "/swagger-ui"
    };

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        for (String excluded : EXCLUDED_PATHS) {
            if (path.startsWith(excluded)) {
                return chain.filter(exchange);
            }
        }

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        log.info("Incoming Request: [{}] {}", request.getMethod(), request.getURI());

        return chain.filter(exchange)
                .doOnSuccess(done -> {
                    log.info("Outgoing Response: [{}] {}", response.getStatusCode(), request.getURI());
                });
    }
}
