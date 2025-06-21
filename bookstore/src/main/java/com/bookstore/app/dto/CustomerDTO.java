package com.bookstore.app.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String emailOffice;
    private String emailPersonal;
    private String phoneNumber;
}