package com.bookstore.app.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long productId;
    private String bookTitle;
    private double bookPrice;
    private Integer bookQuantity;
    private String bookCategory;
    private String bookDesc;
    private String bookImg;
}