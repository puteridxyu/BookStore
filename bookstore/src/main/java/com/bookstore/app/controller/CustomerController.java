package com.bookstore.app.controller;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Flux<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with id: {}", id);
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO);
        return customerService.createCustomer(customerDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer id {} with data: {}", id, customerDTO);
        return customerService.updateCustomer(id, customerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/name")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomerName(
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO) {
        log.info("Patching name for customer id {}: {}", id, customerDTO);
        return customerService.UpdateCustomerName(id, customerDTO)  
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer id: {}", id);
        return customerService.deleteCustomer(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
