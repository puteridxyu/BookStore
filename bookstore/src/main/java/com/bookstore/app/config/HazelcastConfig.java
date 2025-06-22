package com.bookstore.app.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig config = new ClientConfig();
        config.setClusterName("bookstore-cluster");

        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.addAddress("localhost:5701");  
        return HazelcastClient.newHazelcastClient(config);
    }
}
