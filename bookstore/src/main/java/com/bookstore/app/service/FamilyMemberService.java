package com.bookstore.app.service;

import com.bookstore.app.entity.FamilyMember;
import com.bookstore.app.repository.FamilyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

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
