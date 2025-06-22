package com.bookstore.app.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    public static final String CUSTOMER_TOPIC = "customer-events";
    public static final String PRODUCT_TOPIC = "product-events";
    public static final String FAMILY_TOPIC = "family-events";
	public static final String USER_TOPIC = "user-events";
}
