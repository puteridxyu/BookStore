package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.FamilyMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final KafkaProducer kafkaProducer;
    
    @Cacheable(cacheNames = "family-members", key = "#customerId")
    public Flux<FamilyMemberDTO> getAllFamilyMemberByCustomerId(Long customerId) {
        log.info("Fetching family members for customer ID: {}", customerId);
        return repository.findByCustomerId(customerId)
        		.map(this::toDTO);
    }

    @Cacheable(cacheNames = "family-members", key = "#id")
    public Mono<FamilyMemberDTO> getFamilyMemberById(Long id) {
        log.info("Fetching family member by ID: {}", id);
        return repository.findById(id)
        		.map(this::toDTO);
    }

    @CacheEvict(cacheNames = "family-members", allEntries = true)
    public Mono<FamilyMemberDTO> createFamilyMember(FamilyMemberDTO dto) {
        FamilyMember entity = toEntity(dto);
        return repository.save(entity)
                .map(this::toDTO)
                .doOnSuccess(saved -> sendKafkaEvent("created", saved));
    }

    @CacheEvict(cacheNames = "family-members", allEntries = true)
    public Mono<FamilyMemberDTO> updateFamilyMember(Long id, FamilyMemberDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setName(dto.getName());
                    existing.setRelationship(dto.getRelationship());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("updated", updated));
    }

    @CacheEvict(cacheNames = "family-members", allEntries = true)
    public Mono<Void> deleteFamilyMember(Long id) {
    	return repository.findById(id)
                .flatMap(existing ->
                	repository.delete(existing)
                        .doOnSuccess(v -> sendKafkaEvent("deleted", toDTO(existing)))
                );
    }
    
    private void sendKafkaEvent(String action, FamilyMemberDTO dto) {
        String message = String.format(
            "{\"event\":\"%s\",\"name\":\"%s\",\"customerId\":\"%s\",\"id\":%d,\"timestamp\":\"%s\"}",
            action,
            dto.getName(),
            dto.getCustomerId(),
            dto.getFamilyId(),
            java.time.Instant.now()
        );
        kafkaProducer.send(KafkaConfig.FAMILY_TOPIC, message);
        log.info("Kafka event sent: {}", message);
    }

    private FamilyMemberDTO toDTO(FamilyMember entity) {
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setFamilyId(entity.getFamilyId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setName(entity.getName());
        dto.setRelationship(entity.getRelationship());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }

    private FamilyMember toEntity(FamilyMemberDTO dto) {
        FamilyMember entity = new FamilyMember();
        entity.setFamilyId(dto.getFamilyId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setName(dto.getName());
        entity.setRelationship(dto.getRelationship());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}
