package com.bookstore.app.repository;

import com.bookstore.app.entity.FamilyMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FamilyMemberRepository extends ReactiveCrudRepository<FamilyMember, Long> {
    Flux<FamilyMember> findByCustomerId(Long customerId);  
}
