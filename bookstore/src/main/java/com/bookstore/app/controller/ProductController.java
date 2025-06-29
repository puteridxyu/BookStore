package com.bookstore.app.controller;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<ProductDTO> getAllProducts() {
        log.info("Fetching all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO dto) {
        return productService.createProduct(dto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/quantity")
    public Mono<ResponseEntity<ProductDTO>> patchProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.updateProductQuantity(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
