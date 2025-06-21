//package com.bookstore.app.event;
//
//import com.hazelcast.config.Config;
//import com.hazelcast.config.JoinConfig;
//import com.hazelcast.config.NetworkConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CacheConfig {
//
//    @Bean
//    public Config hazelcastConfig() {
//        Config config = new Config();
//        config.setInstanceName("hazelcast-instance");
//
//        NetworkConfig network = config.getNetworkConfig();
//        JoinConfig join = network.getJoin();
//        join.getMulticastConfig().setEnabled(false);
//        join.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");
//
//        return config;
//    }
//}
