package com.bookstore.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long productId;
    private String bookTitle;
    private BigDecimal bookPrice;
    private Integer bookQuantity;
    private String bookCategory;
    private String bookDesc;
    private String bookImg;
}