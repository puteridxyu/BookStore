package com.bookstore.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.main.allow-bean-definition-overriding=true",  
    "kafka.topic.product=test-topic",
    "spring.r2dbc.url=r2dbc:h2:mem:///testdb"
})
class BookStoreApplicationTests {

    @Value("${spring.r2dbc.url:NOT_SET}")
    private String r2dbcUrl;

    @Test
    void contextLoads() {
        assertThat(r2dbcUrl).isNotEqualTo("NOT_SET");
        System.out.println("Loaded spring.r2dbc.url = " + r2dbcUrl);
    }
}
