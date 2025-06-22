package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.entity.User;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.UserRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final KafkaProducer kafkaProducer;
    private final HazelcastInstance hazelcastInstance;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String CACHE_NAME = "users";

    public Mono<UserDTO> getUserById(Long id) {
        IMap<Long, UserDTO> cache = hazelcastInstance.getMap(CACHE_NAME);
        UserDTO cached = cache.get(id);
        if (cached != null) {
            log.info("Cache hit for user ID: {}", id);
            return Mono.just(cached);
        }

        log.info("Cache miss. Fetching user with ID: {} from DB", id);
        return repository.findById(id)
                .map(this::toDTO)
                .doOnNext(dto -> cache.put(dto.getUserId(), dto, 5, TimeUnit.MINUTES));
    }

    public Mono<UserDTO> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toDTO);
    }

    public Mono<UserDTO> login(String username, String rawPassword) {
        return repository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .map(this::toDTO);
    }

    public Mono<UserDTO> createUser(UserDTO dto) {
        User user = toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return repository.save(user)
                .map(this::toDTO)
                .doOnSuccess(saved -> {
                    hazelcastInstance.getMap(CACHE_NAME).put(saved.getUserId(), saved, 5, TimeUnit.MINUTES);
                    sendKafkaEvent("user-created", saved);
                });
    }

    private void sendKafkaEvent(String action, UserDTO dto) {
        String message = String.format(
                "{\"event\":\"%s\",\"username\":\"%s\",\"id\":%d,\"timestamp\":\"%s\"}",
                action, dto.getUsername(), dto.getUserId(), java.time.Instant.now()
        );
        kafkaProducer.send(KafkaConfig.USER_TOPIC, message);
        log.info("Kafka event sent: {}", message);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        return dto;
    }

    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setIsActive(dto.getIsActive());
        return user;
    }
}