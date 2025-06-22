package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.FamilyMemberRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final KafkaProducer kafkaProducer;
    private final HazelcastInstance hazelcastInstance;

    private static final String CACHE_BY_ID = "family-members";
    private static final String CACHE_BY_CUSTOMER = "family-members-by-customer";

    public Flux<FamilyMemberDTO> getAllFamilyMemberByCustomerId(Long customerId) {
        IMap<Integer, String> map = hazelcastInstance.getMap(CACHE_BY_CUSTOMER);

        // simple manual key for now: 1 entry per customer ID
        String cacheKey = "customer:" + customerId;

        // Hazelcast map doesn't support Flux directly
        Object cached = map.get(cacheKey.hashCode());
        if (cached instanceof String cachedJson) {
            log.info("Cache hit for family-members by customerId: {}", customerId);
            return Flux.fromArray(cachedJson.split(";"))
                    .map(this::fromCacheString);
        }

        log.info("Cache miss for customerId: {}, querying DB...", customerId);
        return repository.findByCustomerId(customerId)
                .map(this::toDTO)
                .collectList()
                .doOnNext(list -> {
                    String joined = list.stream()
                            .map(this::toCacheString)
                            .reduce((a, b) -> a + ";" + b)
                            .orElse("");
                    map.put(cacheKey.hashCode(), joined, 5, TimeUnit.MINUTES);
                })
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<FamilyMemberDTO> getFamilyMemberById(Long id) {
        IMap<Long, FamilyMemberDTO> map = hazelcastInstance.getMap(CACHE_BY_ID);
        FamilyMemberDTO cached = map.get(id);
        if (cached != null) {
            log.info("Cache hit for family-member ID: {}", id);
            return Mono.just(cached);
        }

        log.info("Cache miss for ID: {}, querying DB...", id);
        return repository.findById(id)
                .map(this::toDTO)
                .doOnNext(dto -> map.put(dto.getFamilyId(), dto, 5, TimeUnit.MINUTES));
    }

    public Mono<FamilyMemberDTO> createFamilyMember(FamilyMemberDTO dto) {
        FamilyMember entity = toEntity(dto);
        return repository.save(entity)
                .map(this::toDTO)
                .doOnSuccess(saved -> {
                    hazelcastInstance.getMap(CACHE_BY_ID)
                            .put(saved.getFamilyId(), saved, 5, TimeUnit.MINUTES);
                    hazelcastInstance.getMap(CACHE_BY_CUSTOMER)
                            .delete(("customer:" + saved.getCustomerId()).hashCode());  
                    sendKafkaEvent("created", saved);
                });
    }

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
                .doOnSuccess(updated -> {
                    hazelcastInstance.getMap(CACHE_BY_ID)
                            .put(updated.getFamilyId(), updated, 5, TimeUnit.MINUTES);
                    hazelcastInstance.getMap(CACHE_BY_CUSTOMER)
                            .delete(("customer:" + updated.getCustomerId()).hashCode()); 
                    sendKafkaEvent("updated", updated);
                });
    }

    public Mono<Void> deleteFamilyMember(Long id) {
        IMap<Long, FamilyMemberDTO> idMap = hazelcastInstance.getMap(CACHE_BY_ID);
        return repository.findById(id)
                .flatMap(existing ->
                        repository.delete(existing)
                                .doOnSuccess(v -> {
                                    idMap.delete(id);
                                    hazelcastInstance.getMap(CACHE_BY_CUSTOMER)
                                            .delete(("customer:" + existing.getCustomerId()).hashCode());
                                    sendKafkaEvent("deleted", toDTO(existing));
                                }));
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

    private String toCacheString(FamilyMemberDTO dto) {
        return dto.getFamilyId() + "|" + dto.getCustomerId() + "|" + dto.getName();
    }

    private FamilyMemberDTO fromCacheString(String s) {
        String[] parts = s.split("\\|");
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setFamilyId(Long.parseLong(parts[0]));
        dto.setCustomerId(Long.parseLong(parts[1]));
        dto.setName(parts[2]);
        return dto;
    }
}
