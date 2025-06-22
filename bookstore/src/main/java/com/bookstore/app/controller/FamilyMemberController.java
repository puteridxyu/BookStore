package com.bookstore.app.controller;

import com.bookstore.app.dto.FamilyMemberDTO;
import com.bookstore.app.service.FamilyMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers/{customerId}/family-members")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    @GetMapping
    public Flux<FamilyMemberDTO> getAllFamilyMembers(@PathVariable Long customerId) {
        return familyMemberService.getAllFamilyMemberByCustomerId(customerId);
    }

    @PostMapping
    public Mono<ResponseEntity<FamilyMemberDTO>> createFamilyMember(
        @PathVariable Long customerId,
        @Valid @RequestBody FamilyMemberDTO dto
    ) {
        dto.setCustomerId(customerId);
        return familyMemberService.createFamilyMember(dto)
                .map(ResponseEntity::ok);
    }


    @DeleteMapping("/{familyId}")
    public Mono<ResponseEntity<Void>> deleteFamilyMember(@PathVariable Long customerId, @PathVariable Long familyId) {
        return familyMemberService.deleteFamilyMember(familyId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
