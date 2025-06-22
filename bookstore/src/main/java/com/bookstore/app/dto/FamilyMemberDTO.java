package com.bookstore.app.dto;

import lombok.Data;

@Data
public class FamilyMemberDTO {
    private Long id;
    private Long customerId;
    private String memberName;
    private String relationship;
    private String email;
    private String phoneNumber;
}
