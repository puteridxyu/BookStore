package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("customer")
public class Customer {
    @Id
    private Long customerId;
    private String firstName;
    private String lastName;
    private String emailOffice;
    private String emailPersonal;
    private String phoneNumber;
    private LocalDateTime createdAt;
}