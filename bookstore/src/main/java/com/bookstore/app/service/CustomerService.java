package com.bookstore.app.service;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.entity.Customer;
import com.bookstore.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public Flux<CustomerDTO> getAllCustomers() {
        return repository.findAll().map(this::toDTO);
    }

    public Mono<CustomerDTO> createCustomer(CustomerDTO dto) {
        return repository.save(toEntity(dto)).map(this::toDTO);
    }

    public Mono<CustomerDTO> updateCustomer(Long id, CustomerDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmailOffice(dto.getEmailOffice());
                    existing.setEmailPersonal(dto.getEmailPersonal());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return repository.save(existing);
                }).map(this::toDTO);
    }

    public Mono<Void> deleteCustomer(Long id) {
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