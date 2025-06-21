package com.bookstore.app.service;

import com.bookstore.app.dto.CustomerDTO;

import com.bookstore.app.entity.Customer;
import com.bookstore.app.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;  

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService service;

    CustomerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFirstName("Puteri");
        customer.setLastName("Ali");

        when(repository.findAll()).thenReturn(Flux.just(customer));

        StepVerifier.create(service.getAllCustomers())
                .expectNextMatches(dto -> dto.getFirstName().equals("Puteri"))
                .verifyComplete();
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("Laila");
        dto.setLastName("Hassan");

        Customer customer = new Customer();
        customer.setFirstName("Laila");
        customer.setLastName("Hassan");

        when(repository.save(any())).thenReturn(Mono.just(customer));

        StepVerifier.create(service.createCustomer(dto))
                .expectNextMatches(res -> res.getFirstName().equals("Laila"))
                .verifyComplete();
    }
}

