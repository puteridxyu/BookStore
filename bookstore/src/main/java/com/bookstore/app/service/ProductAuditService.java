package com.bookstore.app.service;

import com.bookstore.app.entity.ProductAudit;
import com.bookstore.app.repository.CustomerRepository;
import com.bookstore.app.repository.ProductAuditRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAuditService {

    private final ProductAuditRepository productAuditRepository;

    public List<ProductAudit> getAll() {
        return productAuditRepository.findAll();
    }

    public ProductAudit save(ProductAudit audit) {
        return productAuditRepository.save(audit);
    }
}
