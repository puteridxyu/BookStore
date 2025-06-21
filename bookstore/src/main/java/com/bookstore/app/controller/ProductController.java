package com.bookstore.app.controller;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PatchMapping("/{id}/quantity")
    public Mono<ProductDTO> updateQuantity(@PathVariable Long id, @RequestBody ProductDTO dto) {
        log.info("Updating quantity for product {}", id);
        return productService.updateProductQuantity(id, dto.getBookQuantity());
    }

    @PostMapping
    public Mono<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        log.info("Creating product: {}", dto);
        return productService.createProduct(dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        return productService.deleteProduct(id);
    }
}
