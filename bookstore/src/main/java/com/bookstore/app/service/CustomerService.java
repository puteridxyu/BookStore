package com.bookstore.app.service;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public Mono<CustomerDTO> getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return repository.findById(id)
                .map(this::toDTO);
    }

    public Mono<CustomerDTO> createCustomer(CustomerDTO dto) {
        Customer entity = toEntity(dto);
        return repository.save(entity)
                .map(this::toDTO)
                .doOnSuccess(savedDto -> {
                    String msg = "New customer created: " + savedDto.getFirstName() + " " + savedDto.getLastName();
                    log.info(msg);
                    kafkaProducer.send("customer-topic", msg);
                });
    }

    public Mono<CustomerDTO> updateCustomer(Long id, CustomerDTO dto) {
        log.info("Updating customer with ID: {}", id);
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmailOffice(dto.getEmailOffice());
                    existing.setEmailPersonal(dto.getEmailPersonal());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return repository.save(existing);
                })
                .map(this::toDTO);
    }

    public Mono<CustomerDTO> updateCustomerName(Long id, CustomerDTO dto) {
        log.info("Updating name for customer ID: {}", id);
        return repository.findById(id)
                .flatMap(existing -> {
                    if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updatedDto -> {
                    String msg = "Customer name updated: " + updatedDto.getFirstName() + " " + updatedDto.getLastName();
                    log.info(msg);
                    kafkaProducer.send("customer-topic", msg);
                });
    }

    public Mono<Void> deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        return repository.deleteById(id);
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