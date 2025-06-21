//package com.bookstore.app.event;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KafkaProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessage(String topic, String message) {
//        log.info("Sending message to Kafka topic {}: {}", topic, message);
//        kafkaTemplate.send(topic, message);
//    }
//}
