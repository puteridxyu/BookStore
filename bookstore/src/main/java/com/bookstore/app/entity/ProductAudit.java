package com.bookstore.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proAuditId;

    @Column(nullable = false, length = 20)
    private String eventType;

    @Builder.Default
    @Column
    private LocalDateTime changeTime = LocalDateTime.now(); 

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}