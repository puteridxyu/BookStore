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

    @PostMapping
    public Mono<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO);
        return customerService.createCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer id {} with data: {}", id, customerDTO);
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer id: {}", id);
        return customerService.deleteCustomer(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
