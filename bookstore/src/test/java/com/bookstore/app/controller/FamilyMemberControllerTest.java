package com.bookstore.app.controller;

import com.bookstore.app.config.TestSecurityConfig;
import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.service.FamilyMemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(FamilyMemberController.class)
@Import(TestSecurityConfig.class)  
public class FamilyMemberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FamilyMemberService familyMemberService;

    private FamilyMemberDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new FamilyMemberDTO();
        sampleDto.setFamilyId(1L);
        sampleDto.setCustomerId(10L);
        sampleDto.setName("Alice");
        sampleDto.setRelationship("Child");  
        sampleDto.setEmail("alice@example.com");
        sampleDto.setPhoneNumber("123456789");
    }

    @Test
    void testGetAllFamilyMembers() {
        when(familyMemberService.getAllFamilyMemberByCustomerId(10L)).thenReturn(Flux.just(sampleDto));

        webTestClient.get()
                .uri("/api/customers/10/family-members")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FamilyMemberDTO.class)
                .hasSize(1)
                .value(list -> {
                    assert list.get(0).getName().equals("Alice");
                    assert list.get(0).getRelationship().equals("Child");
                });
    }

    @Test
    void testCreateFamilyMember() {
        when(familyMemberService.createFamilyMember(any(FamilyMemberDTO.class))).thenReturn(Mono.just(sampleDto));

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
        when(familyMemberService.deleteFamilyMember(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/customers/10/family-members/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
