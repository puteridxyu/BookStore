package com.bookstore.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(columnDefinition = "TEXT")
    private String bookDesc;

    @Column(columnDefinition = "TEXT")
    private String bookImg;

    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAudit> audits;

    public enum BookCategory {
        ENGLISH("English"),
        LITERATURE("Literature"),
        STUDENT_REFERENCE("Student Reference"),
        SCIENCE("Science"),
        TECHNOLOGY("Technology"),
        CHILDREN("Children"),
        RELIGION("Religion"),
        COMICS("Comics"),
        HISTORY("History"),
        BUSINESS("Business"),
        SELF_HELP("Self Help"),
        COOKING("Cooking"),
        HEALTH_WELLNESS("Health & Wellness"),
        TRAVEL("Travel"),
        MOTIVATION("Motivation"),
        ART_DESIGN("Art & Design");

        private final String label;

        BookCategory(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}