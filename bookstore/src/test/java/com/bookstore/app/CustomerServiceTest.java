package com.bookstore.app.service;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() {
        Customer c = new Customer();
        c.setCustomerId(1L);
        c.setFirstName("John");
        c.setLastName("Doe");

        when(customerRepository.findAll()).thenReturn(Flux.just(c));

        StepVerifier.create(customerService.getAllCustomers())
                .expectNextMatches(dto -> dto.getFirstName().equals("John"))
                .verifyComplete();
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("Jane");

        Customer entity = new Customer();
        entity.setFirstName("Jane");

        when(customerRepository.save(any())).thenReturn(Mono.just(entity));

        StepVerifier.create(customerService.createCustomer(dto))
                .expectNextMatches(res -> res.getFirstName().equals("Jane"))
                .verifyComplete();
    }
}