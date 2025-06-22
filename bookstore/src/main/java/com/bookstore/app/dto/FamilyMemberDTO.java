package com.bookstore.app.dto;

import lombok.Data;

@Data
public class FamilyMemberDTO {
    private Long familyId;
    private Long customerId;
    private String name;
    private String relationship;
    private String email;
    private String phoneNumber;
}
