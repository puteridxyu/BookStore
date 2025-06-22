package com.bookstore.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.service.CustomerService;

@WebFluxTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO sampleDto;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	
        sampleDto = new CustomerDTO();
        sampleDto.setCustomerId(1L);
        sampleDto.setFirstName("John");
        sampleDto.setLastName("Doe");
        sampleDto.setEmailOffice("john.doe@office.com");
        sampleDto.setEmailPersonal("john.doe@gmail.com");
        sampleDto.setPhoneNumber("123456789");
        
    }

    @Test
    void testGetAllCustomers() {
        Mockito.when(customerService.getAllCustomers()).thenReturn(Flux.just(sampleDto));

        webTestClient.get().uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDTO.class).hasSize(1);
    }

    @Test
    void testGetCustomerById() {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Mono.just(sampleDto));

        webTestClient.get().uri("/api/customers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testCreateCustomer() {
        Mockito.when(customerService.createCustomer(Mockito.any())).thenReturn(Mono.just(sampleDto));

        webTestClient.post().uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testUpdateCustomer() {
        Mockito.when(customerService.updateCustomer(Mockito.eq(1L), Mockito.any())).thenReturn(Mono.just(sampleDto));

        webTestClient.put().uri("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testUpdateCustomerName() {
        CustomerDTO patchDto = new CustomerDTO();
        patchDto.setFirstName("Patched");

        Mockito.when(customerService.updateCustomerName(Mockito.eq(1L), Mockito.any())).thenReturn(Mono.just(patchDto));

        webTestClient.patch().uri("/api/customers/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(patchDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testDeleteCustomer() {
        Mockito.when(customerService.deleteCustomer(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/customers/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
