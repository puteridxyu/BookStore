package com.bookstore.app.service;

import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.repository.CustomerRepository;
import com.bookstore.app.repository.FamilyMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;

    public List<FamilyMember> getAll() {
        return familyMemberRepository.findAll();
    }

    public FamilyMember getById(Long id) {
        return familyMemberRepository.findById(id).orElse(null);
    }

    public FamilyMember save(FamilyMember familyMember) {
        return familyMemberRepository.save(familyMember);
    }

    public void delete(Long id) {
        familyMemberRepository.deleteById(id);
    }
    
    public List<FamilyMember> getByCustomerId(Long customerId) {
        return familyMemberRepository.findByCustomerCustomerId(customerId);
    }
}
