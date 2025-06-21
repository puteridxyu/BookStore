
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        log.info("Incoming request: {} {}", request.getMethod(), request.getURI());

        return chain.filter(exchange)
                .doOnSuccessOrError((done, ex) -> {
                    if (ex == null) {
                        log.info("Response status: {}", response.getStatusCode());
                    } else {
                        log.error("Error during request: ", ex);
                    }
                });
    }
}
