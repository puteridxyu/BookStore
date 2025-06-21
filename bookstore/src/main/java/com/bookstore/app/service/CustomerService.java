package com.bookstore.app.service;

import com.bookstore.app.entity.Customer;
import com.bookstore.app.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDetails.getFirstName());
                    customer.setLastName(customerDetails.getLastName());
                    customer.setEmailOffice(customerDetails.getEmailOffice());
                    customer.setEmailPersonal(customerDetails.getEmailPersonal());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + id + " not found"));
    }


    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}

