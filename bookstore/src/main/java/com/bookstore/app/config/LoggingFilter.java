package com.bookstore.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger") || path.startsWith("/swagger-ui.html")) {
            return chain.filter(exchange); // Allow it
        }

        // Logging logic...
        return chain.filter(exchange);
    }
}

