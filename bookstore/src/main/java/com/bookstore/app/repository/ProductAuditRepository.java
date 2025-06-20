package com.bookstore.app.repository;

import com.bookstore.app.entity.ProductAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAuditRepository extends JpaRepository<ProductAudit, Long> {
}
