package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user")
public class User {
    @Id
    private Long userId;
    private String username;
    private String password;
    private String email;
    private Boolean isActive;
    private Long customerId;
}