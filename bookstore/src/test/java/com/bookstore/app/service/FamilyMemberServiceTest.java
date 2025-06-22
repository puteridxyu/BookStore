package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
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
    private FamilyMemberRepository repository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private FamilyMemberService service;

    private FamilyMember sampleEntity;
    private FamilyMemberDTO sampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEntity = new FamilyMember();
        sampleEntity.setFamilyId(1L);
        sampleEntity.setCustomerId(1L);
        sampleEntity.setName("Aisyah");

        sampleDTO = new FamilyMemberDTO();
        sampleDTO.setFamilyId(1L);
        sampleDTO.setCustomerId(1L);
        sampleDTO.setName("Aisyah");
        
    }

    @Test
    void testGetAllFamilyMemberByCustomerId() {
        when(repository.findByCustomerId(1L)).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(service.getAllFamilyMemberByCustomerId(1L))
                .expectNextMatches(dto -> dto.getFamilyId() == 1L)
                .verifyComplete();
    }

    @Test
    void testGetFamilyMemberById() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.getFamilyMemberById(1L))
                .expectNextMatches(dto -> dto.getFamilyId() == 1L)
                .verifyComplete();
    }

    @Test
    void testCreateFamilyMember() {
        when(repository.save(any())).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.createFamilyMember(sampleDTO))
                .expectNextMatches(dto -> dto.getFamilyId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.FAMILY_TOPIC), contains("created"));
    }

    @Test
    void testUpdateFamilyMember() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.save(any())).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.updateFamilyMember(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getFamilyId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.FAMILY_TOPIC), contains("updated"));
    }

    @Test
    void testDeleteFamilyMember() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteFamilyMember(1L))
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.FAMILY_TOPIC), contains("deleted"));
    }
}
