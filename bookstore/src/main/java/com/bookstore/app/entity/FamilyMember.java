package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("family_member")
public class FamilyMember {
    @Id
    private Long familyId;
    private Long customerId;
    private String name;
    private String relationship;
    private String email;
    private String phoneNumber;
}
