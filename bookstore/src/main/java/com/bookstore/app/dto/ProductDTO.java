package com.bookstore.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Book title is required")
    private String bookTitle;

    @DecimalMin(value = "0.01", message = "Book price must be greater than 0")
    private double bookPrice;

    private Integer bookQuantity;

    @NotBlank(message = "Book category is required")
    private String bookCategory;

    private String bookDesc;

    private String bookImg;
}
