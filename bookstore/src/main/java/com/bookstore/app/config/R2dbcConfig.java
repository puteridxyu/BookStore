package com.bookstore.app.config;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class R2dbcConfig {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, env.getProperty("db.host", "localhost")) //
                .option(PORT, Integer.parseInt(env.getProperty("db.port", "5432")))
                .option(USER, env.getProperty("db.user", "postgres"))
                .option(PASSWORD, env.getProperty("db.pass", "eAT6&kILL20"))
                .option(DATABASE, env.getProperty("db.name", "bookstore"))
                .build()
        );
    }
}
