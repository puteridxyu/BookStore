package com.bookstore.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDTO {

    private Long customerId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid office email format")
    private String emailOffice;

    @NotBlank(message = "Personal email is required")
    @Email(message = "Invalid personal email format")
    private String emailPersonal;

    private String phoneNumber;
}
