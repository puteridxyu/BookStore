package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("customer")
public class Customer {
    @Id
    @Column("customer_id")
    private Long customerId;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email_office")
    private String emailOffice;

    @Column("email_personal")
    private String emailPersonal;

    @Column("phone_number")
    private String phoneNumber;

    @Column("created_at")
    private LocalDateTime createdAt;
}
