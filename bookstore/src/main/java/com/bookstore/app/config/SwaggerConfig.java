package com.bookstore.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookstoreOpenAPI() {
        return new OpenAPI()
        	.openapi("3.1.0")
            .info(new Info()
                .title("Bookstore API")
                .version("1.0.0")
                .description("Customer & Product Management"));
    }
}
