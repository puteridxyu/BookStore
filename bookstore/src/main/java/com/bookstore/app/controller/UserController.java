package com.bookstore.app.controller;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username/{username}")
    public Mono<ResponseEntity<UserDTO>> getByUsername(@PathVariable String username) {
        log.info("Fetching user by username: {}", username);
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<UserDTO>> login(@RequestParam String username, @RequestParam String password) {
        log.info("Login attempt for username: {}", username);
        return userService.login(username, password)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser(@Valid @RequestBody UserDTO dto) {
        log.info("Creating user: {}", dto.getUsername());
        return userService.createUser(dto)
                .map(ResponseEntity::ok);
    }
}
