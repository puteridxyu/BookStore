package com.bookstore.app.service;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private CustomerService customerService;

    private Customer sampleEntity;
    private CustomerDTO sampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEntity = new Customer();
        sampleEntity.setCustomerId(1L);
        sampleEntity.setFirstName("John");
        sampleEntity.setLastName("Doe");

        sampleDTO = new CustomerDTO();
        sampleDTO.setCustomerId(1L);
        sampleDTO.setFirstName("John");
        sampleDTO.setLastName("Doe");
    }

    @Test
    void testGetAllCustomers() {
        when(repository.findAll()).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(customerService.getAllCustomers())
                .expectNextMatches(dto -> dto.getFirstName().equals("John"))
                .verifyComplete();
    }

    @Test
    void testGetCustomerById() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(customerService.getCustomerById(1L))
                .expectNextMatches(dto -> dto.getCustomerId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testCreateCustomer() {
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(customerService.createCustomer(sampleDTO))
                .expectNextMatches(dto -> dto.getFirstName().equals("John"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("customer-topic"), contains("New customer created"));
    }

    @Test
    void testUpdateCustomer() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(customerService.updateCustomer(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getFirstName().equals("John"))
                .verifyComplete();
    }

    @Test
    void testUpdateCustomerName() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(customerService.updateCustomerName(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getFirstName().equals("John"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("customer-topic"), contains("Customer name updated"));
    }

    @Test
    void testDeleteCustomer() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.deleteCustomer(1L))
                .verifyComplete();
    }
}