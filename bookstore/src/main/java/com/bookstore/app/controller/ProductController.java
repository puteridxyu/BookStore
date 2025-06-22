package com.bookstore.app.controller;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.service.ProductService;
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
        log.info("Fetching product with id {}", id);
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@RequestBody ProductDTO dto) {
        log.info("Creating product: {}", dto);
        return productService.createProduct(dto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        log.info("Updating product with id {}: {}", id, dto);
        return productService.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/quantity")
    public Mono<ResponseEntity<ProductDTO>> patchProductQuantity(@PathVariable Long id, @RequestBody ProductDTO dto) {
        log.info("Patching quantity for product id {}: {}", id, dto);
        return productService.updateProductQuantity(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        return productService.deleteProduct(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
