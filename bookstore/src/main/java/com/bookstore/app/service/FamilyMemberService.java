package com.bookstore.app.service;

import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.FamilyMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;
    private final KafkaProducer kafkaProducer;

    public Flux<FamilyMemberDTO> getAllByCustomerId(Long customerId) {
        log.info("Fetching family members for customer ID: {}", customerId);
        return familyMemberRepository.findByCustomerId(customerId)
                .map(this::toDTO);
    }

    public Mono<FamilyMemberDTO> getById(Long id) {
        log.info("Fetching family member by ID: {}", id);
        return familyMemberRepository.findById(id)
                .map(this::toDTO);
    }

    public Mono<FamilyMemberDTO> create(FamilyMemberDTO dto) {
        FamilyMember entity = toEntity(dto);
        return familyMemberRepository.save(entity)
                .map(this::toDTO)
                .doOnSuccess(saved -> {
                    String msg = "Family member added: " + saved.getMemberName();
                    log.info(msg);
                    kafkaProducer.send("family-topic", msg);
                });
    }

    public Mono<FamilyMemberDTO> update(Long id, FamilyMemberDTO dto) {
        log.info("Updating family member with ID: {}", id);
        return familyMemberRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(dto.getMemberName());
                    existing.setRelationship(dto.getRelationship());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return familyMemberRepository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> {
                    String msg = "Family member updated: " + updated.getMemberName();
                    log.info(msg);
                    kafkaProducer.send("family-topic", msg);
                });
    }

    public Mono<Void> delete(Long id) {
        log.info("Deleting family member with ID: {}", id);
        return familyMemberRepository.deleteById(id)
                .doOnSuccess(unused -> {
                    String msg = "Family member deleted with ID: " + id;
                    log.info(msg);
                    kafkaProducer.send("family-topic", msg);
                });
    }

    private FamilyMemberDTO toDTO(FamilyMember entity) {
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setId(entity.getFamilyId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setMemberName(entity.getName());
        dto.setRelationship(entity.getRelationship());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }

    private FamilyMember toEntity(FamilyMemberDTO dto) {
        FamilyMember entity = new FamilyMember();
        entity.setFamilyId(dto.getId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setName(dto.getMemberName());
        entity.setRelationship(dto.getRelationship());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}
