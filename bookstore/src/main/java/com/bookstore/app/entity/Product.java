package com.bookstore.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 255)
    private String bookTitle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bookPrice;

    @Column(nullable = false)
    private Integer bookQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private BookCategory bookCategory;

    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "product", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<ProductAudit> audits;

    public enum BookCategory {
        ENGLISH,
        LITERATURE,
        STUDENT_REFERENCE,
        SCIENCE,
        TECHNOLOGY,
        CHILDREN,
        RELIGION,
        COMICS,
        HISTORY,
        BUSINESS,
        SELF_HELP,
        COOKING,
        HEALTH_WELLNESS,
        TRAVEL,
        MOTIVATION,
        ART_DESIGN
    }
}
