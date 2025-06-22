package com.bookstore.app.controller;


import com.bookstore.app.config.TestSecurityConfig;
import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(UserController.class) 
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    private UserDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new UserDTO();
        sampleDto.setUserId(1L);
        sampleDto.setUsername("puteri");
        sampleDto.setPassword("secret");
        sampleDto.setRole(1);
        sampleDto.setEmail("puteri@example.com");
        sampleDto.setIsActive(true);
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(Mono.just(sampleDto));

        webTestClient.get().uri("/api/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(99L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/users/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetByUsername() {
        when(userService.findByUsername("puteri")).thenReturn(Mono.just(sampleDto));

        webTestClient.get().uri("/api/users/by-username/puteri")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testGetByUsername_NotFound() {
        when(userService.findByUsername("ghost")).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/users/by-username/ghost")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testLogin_Success() {
        when(userService.login("puteri", "secret")).thenReturn(Mono.just(sampleDto));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/login")
                        .queryParam("username", "puteri")
                        .queryParam("password", "secret")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testLogin_Failure() {
        when(userService.login("puteri", "wrongpass")).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/login")
                        .queryParam("username", "puteri")
                        .queryParam("password", "wrongpass")
                        .build())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(Mono.just(sampleDto));

        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(sampleDto);
    }
}
