package com.bookstore.app.service;

import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.FamilyMemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FamilyMemberServiceTest {

    @Mock
    private FamilyMemberRepository familyMemberRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private FamilyMemberService familyMemberService;

    private FamilyMember sampleEntity;
    private FamilyMemberDTO sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDto = new FamilyMemberDTO();
        sampleDto.setId(1L);
        sampleDto.setCustomerId(10L);
        sampleDto.setMemberName("Alice");
        sampleDto.setRelationship("Daughter");
        sampleDto.setEmail("alice@example.com");
        sampleDto.setPhoneNumber("123456789");

        sampleEntity = new FamilyMember();
        sampleEntity.setFamilyId(1L);
        sampleEntity.setCustomerId(10L);
        sampleEntity.setName("Alice");
        sampleEntity.setRelationship("Daughter");
        sampleEntity.setEmail("alice@example.com");
        sampleEntity.setPhoneNumber("123456789");
    }

    @Test
    void testGetAllByCustomerId() {
        when(familyMemberRepository.findByCustomerId(10L)).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(familyMemberService.getAllByCustomerId(10L))
                .expectNextMatches(fm -> fm.getMemberName().equals("Alice"))
                .verifyComplete();
    }

    @Test
    void testGetById() {
        when(familyMemberRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(familyMemberService.getById(1L))
                .expectNextMatches(fm -> fm.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testCreate() {
        when(familyMemberRepository.save(any(FamilyMember.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(familyMemberService.create(sampleDto))
                .expectNextMatches(fm -> fm.getEmail().equals("alice@example.com"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("family-topic"), contains("added"));
    }

    @Test
    void testUpdate() {
        when(familyMemberRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(familyMemberRepository.save(any(FamilyMember.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(familyMemberService.update(1L, sampleDto))
                .expectNextMatches(fm -> fm.getRelationship().equals("Daughter"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("family-topic"), contains("updated"));
    }

    @Test
    void testDelete() {
        when(familyMemberRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(familyMemberService.delete(1L))
                .verifyComplete();

        verify(kafkaProducer).send(eq("family-topic"), contains("deleted"));
    }
}
