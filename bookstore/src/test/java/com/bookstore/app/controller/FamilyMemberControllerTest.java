package com.bookstore.app.controller;

import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.service.FamilyMemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest(FamilyMemberController.class)
public class FamilyMemberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FamilyMemberService familyMemberService;

    private FamilyMemberDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new FamilyMemberDTO();
        sampleDto.setId(1L);
        sampleDto.setCustomerId(10L);
        sampleDto.setMemberName("Alice");
        sampleDto.setRelationship("Daughter");
        sampleDto.setEmail("alice@example.com");
        sampleDto.setPhoneNumber("123456789");
    }

    @Test
    void testGetAllFamilyMembers() {
        when(familyMemberService.getAllByCustomerId(10L)).thenReturn(Flux.just(sampleDto));

        webTestClient.get()
                .uri("/api/customers/10/family-members")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FamilyMemberDTO.class)
                .hasSize(1)
                .contains(sampleDto);
    }

    @Test
    void testCreateFamilyMember() {
        when(familyMemberService.create(any(FamilyMemberDTO.class))).thenReturn(Mono.just(sampleDto));

        webTestClient.post()
                .uri("/api/customers/10/family-members")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FamilyMemberDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testDeleteFamilyMember() {
        when(familyMemberService.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/customers/10/family-members/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
