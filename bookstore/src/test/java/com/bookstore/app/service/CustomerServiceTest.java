package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.CustomerRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

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

public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private HazelcastInstance hazelcastInstance;

    @Mock
    private IMap<Object, Object> cache;

    @InjectMocks
    private CustomerService service;

    private Customer sampleEntity;
    private CustomerDTO sampleDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        sampleEntity = new Customer();
        sampleEntity.setCustomerId(1L);
        sampleEntity.setFirstName("Puteri");
        sampleEntity.setLastName("Aisyah");

        sampleDTO = new CustomerDTO();
        sampleDTO.setCustomerId(1L);
        sampleDTO.setFirstName("Puteri");
        sampleDTO.setLastName("Aisyah");

        when(hazelcastInstance.getMap("customers")).thenReturn(cache);
    }

    @Test
    public void testGetAllCustomers() {
        when(repository.findAll()).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(service.getAllCustomers())
                .expectNextMatches(dto -> dto.getFirstName().equals("Puteri"))
                .verifyComplete();
    }

    @Test
    public void testGetCustomerById() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.getCustomerById(1L))
                .expectNextMatches(dto -> dto.getCustomerId() == 1L)
                .verifyComplete();
    }

    @Test
    public void testCreateCustomer() {
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.createCustomer(sampleDTO))
                .expectNextMatches(dto -> dto.getCustomerId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.CUSTOMER_TOPIC), contains("created"));
    }

    @Test
    public void testUpdateCustomer() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.updateCustomer(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getCustomerId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.CUSTOMER_TOPIC), contains("updated"));
    }

    @Test
    public void testDeleteCustomer() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.delete(sampleEntity)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteCustomer(1L))
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.CUSTOMER_TOPIC), contains("deleted"));
    }
}
