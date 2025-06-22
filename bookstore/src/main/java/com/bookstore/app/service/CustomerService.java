package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final KafkaProducer kafkaProducer;
    
    public Flux<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers from DB");
        return repository.findAll().map(this::toDTO);
    }

    @Cacheable(cacheNames = "customers", key = "#id")
    public Mono<CustomerDTO> getCustomerById(Long id) {
        log.info("Fetching customer with ID: {} from DB", id);
        return repository.findById(id).map(this::toDTO);
    }

    @CacheEvict(cacheNames = "customers", key = "#result.customerId", condition = "#result != null")
    public Mono<CustomerDTO> createCustomer(CustomerDTO dto) {
        Customer entity = toEntity(dto);
        return repository.save(entity)
        		.map(this::toDTO)
                .doOnSuccess(saved -> sendKafkaEvent("created", saved));
    }

    @CacheEvict(cacheNames = "customers", key = "#id")
    public Mono<CustomerDTO> updateCustomer(Long id, CustomerDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmailOffice(dto.getEmailOffice());
                    existing.setEmailPersonal(dto.getEmailPersonal());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("updated", updated));
    }

    @CacheEvict(cacheNames = "customers", key = "#id")
    public Mono<CustomerDTO> updateCustomerName(Long id, CustomerDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("customer-updated", updated));
    }

    @CacheEvict(cacheNames = "customers", key = "#id")
    public Mono<Void> deleteCustomer(Long id) {
    	return repository.findById(id)
                .flatMap(existing ->
                	repository.delete(existing)
                        .doOnSuccess(v -> sendKafkaEvent("deleted", toDTO(existing)))
                );
    }

    private void sendKafkaEvent(String action, CustomerDTO dto) {
        String message = String.format(
            "{\"event\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"id\":%d,\"timestamp\":\"%s\"}",
            action,
            dto.getFirstName(),
            dto.getLastName(),
            dto.getCustomerId(),
            java.time.Instant.now()
        );
        kafkaProducer.send(KafkaConfig.CUSTOMER_TOPIC, message);
        log.info("Kafka event sent: {}", message);
    }
    
    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmailOffice(customer.getEmailOffice());
        dto.setEmailPersonal(customer.getEmailPersonal());
        dto.setPhoneNumber(customer.getPhoneNumber());
        return dto;
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmailOffice(dto.getEmailOffice());
        entity.setEmailPersonal(dto.getEmailPersonal());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}
