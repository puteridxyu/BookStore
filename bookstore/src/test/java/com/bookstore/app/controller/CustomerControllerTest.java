package com.bookstore.app.controller;

import com.bookstore.app.config.TestSecurityConfig;  
import com.bookstore.app.dto.CustomerDTO;
import com.bookstore.app.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(CustomerController.class)
@Import(TestSecurityConfig.class) 
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO sampleDto;

    @BeforeEach
    void setUp() {
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
        when(customerService.getAllCustomers()).thenReturn(Flux.just(sampleDto));

        webTestClient.get().uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDTO.class)
                .hasSize(1);
    }

    @Test
    void testGetCustomerById() {
        when(customerService.getCustomerById(1L)).thenReturn(Mono.just(sampleDto));

        webTestClient.get().uri("/api/customers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testCreateCustomer() {
        when(customerService.createCustomer(any())).thenReturn(Mono.just(sampleDto));

        webTestClient.post().uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testUpdateCustomer() {
        when(customerService.updateCustomer(eq(1L), any())).thenReturn(Mono.just(sampleDto));

        webTestClient.put().uri("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(sampleDto);
    }

    @Test
    void testUpdateCustomerName() {
        CustomerDTO updatedNameDto = new CustomerDTO();
        updatedNameDto.setCustomerId(1L);
        updatedNameDto.setFirstName("Jane");
        updatedNameDto.setLastName("Smith");

        updatedNameDto.setEmailOffice("jane.smith@office.com");
        updatedNameDto.setEmailPersonal("jane.smith@gmail.com");
        updatedNameDto.setPhoneNumber("987654321");

        when(customerService.updateCustomerName(eq(1L), any())).thenReturn(Mono.just(updatedNameDto));

        webTestClient.patch().uri("/api/customers/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedNameDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(updatedNameDto);
    }


    @Test
    void testDeleteCustomer() {
        when(customerService.deleteCustomer(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/customers/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
