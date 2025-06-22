package com.bookstore.app.controller;

import com.bookstore.app.config.TestSecurityConfig;
import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(ProductController.class)
@Import(TestSecurityConfig.class) 
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    private ProductDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new ProductDTO();
        sampleDto.setProductId(1L);
        sampleDto.setBookTitle("Java Mastery");
        sampleDto.setBookPrice(99.99);
        sampleDto.setBookQuantity(50);
        sampleDto.setBookCategory("Programming");
        sampleDto.setBookDesc("Complete Java Guide");
        sampleDto.setBookImg("image.png");
    }

    @Test
    void testGetAllProducts() {
        Mockito.when(productService.getAllProducts()).thenReturn(Flux.just(sampleDto));

        webTestClient.get().uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .hasSize(1);
    }

    @Test
    void testGetProductById_Found() {
        Mockito.when(productService.getProductById(1L)).thenReturn(Mono.just(sampleDto));

        webTestClient.get().uri("/api/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .value(dto -> {
                    assert dto.getProductId().equals(1L);
                    assert dto.getBookTitle().equals("Java Mastery");
                });
    }

    @Test
    void testGetProductById_NotFound() {
        Mockito.when(productService.getProductById(99L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/products/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateProduct() {
        Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(Mono.just(sampleDto));

        webTestClient.post().uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .value(dto -> {
                    assert dto.getBookTitle().equals("Java Mastery");
                });
    }

    @Test
    void testCreateProduct_Invalid() {
        ProductDTO invalid = new ProductDTO(); 

        webTestClient.post().uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalid)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateProduct() {
        Mockito.when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(Mono.just(sampleDto));

        webTestClient.put().uri("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .value(dto -> {
                    assert dto.getBookCategory().equals("Programming");
                });
    }

    @Test
    void testUpdateProduct_NotFound() {
        Mockito.when(productService.updateProduct(eq(99L), any(ProductDTO.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/api/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchProductQuantity() {
        Mockito.when(productService.updateProductQuantity(eq(1L), any(ProductDTO.class))).thenReturn(Mono.just(sampleDto));

        webTestClient.patch().uri("/api/products/1/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .value(dto -> {
                    assert dto.getBookQuantity() == 50;
                });
    }

    @Test
    void testPatchProductQuantity_NotFound() {
        Mockito.when(productService.updateProductQuantity(eq(99L), any(ProductDTO.class))).thenReturn(Mono.empty());

        webTestClient.patch().uri("/api/products/99/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteProduct() {
        Mockito.when(productService.deleteProduct(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/products/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
