package com.bookstore.app.controller;

import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family-members")
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    @GetMapping
    public List<FamilyMember> getAll() {
        return familyMemberService.getAll();
    }

    @GetMapping("/{id}")
    public FamilyMember getById(@PathVariable Long id) {
        return familyMemberService.getById(id);
    }

    @PostMapping
    public FamilyMember create(@RequestBody FamilyMember member) {
        return familyMemberService.save(member);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        familyMemberService.delete(id);
    }
}
