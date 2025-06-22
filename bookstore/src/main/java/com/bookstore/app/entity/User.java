package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("userid")
public class User {

    @Id
    @Column("user_id")
    private Long userId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("email")
    private String email;

    @Column("role")
    private Integer role; // 1 = ADMIN by default

    @Column("is_active")
    private Boolean isActive;
}
