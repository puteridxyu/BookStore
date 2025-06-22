package com.bookstore.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FamilyMemberDTO {

    private Long familyId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Relationship is required")
    private String relationship;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;
}
