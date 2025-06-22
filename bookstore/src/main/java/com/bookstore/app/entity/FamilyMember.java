package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("family_member")
public class FamilyMember {

    @Id
    @Column("family_id")
    private Long familyId;

    @Column("customer_id")
    private Long customerId;

    @Column("name")
    private String name;

    @Column("relationship")
    private String relationship;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;
}
