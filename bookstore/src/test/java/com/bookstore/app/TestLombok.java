package com.bookstore.app;

import com.bookstore.app.entity.Customer;

public class TestLombok {
	
	public static void main(String[] args) {
        Customer customer = Customer.builder()
                .firstName("Puteri")
                .lastName("Zafri")
                .emailOffice("puteri@company.com")
                .emailPersonal("puteri@gmail.com")
                .build();

        System.out.println("First Name: " + customer.getFirstName());
        System.out.println("Last Name: " + customer.getLastName());
    }
	

}
