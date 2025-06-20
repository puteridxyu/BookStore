package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("product")
public class Product {
    @Id
    private Long productId;
    private String bookTitle;
    private BigDecimal bookPrice;
    private Integer bookQuantity;
    private String bookCategory;
    private String bookDesc;
    private String bookImg;
    private LocalDateTime createdAt;
}