package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("product")
public class Product {
    @Id
    @Column("product_id")
    private Long productId;

    @Column("book_title")
    private String bookTitle;

    @Column("book_price")
    private double bookPrice;

    @Column("book_quantity")
    private Integer bookQuantity;

    @Column("book_category")
    private String bookCategory;

    @Column("book_desc")
    private String bookDesc;

    @Column("book_img")
    private String bookImg;

    @Column("created_at")
    private LocalDateTime createdAt;
}