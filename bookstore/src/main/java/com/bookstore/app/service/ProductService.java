package com.bookstore.app.service;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.entity.Product;
import com.bookstore.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<ProductDTO> getAllProducts() {
        return repository.findAll().map(this::toDTO);
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto) {
        return repository.save(toEntity(dto)).map(this::toDTO);
    }

    public Mono<ProductDTO> updateProductQuantity(Long id, Integer newQty) {
        return repository.findById(id)
                .flatMap(product -> {
                    product.setBookQuantity(newQty);
                    return repository.save(product);
                }).map(this::toDTO);
    }

    public Mono<Void> deleteProduct(Long id) {
        return repository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setBookTitle(product.getBookTitle());
        dto.setBookPrice(product.getBookPrice());
        dto.setBookQuantity(product.getBookQuantity());
        dto.setBookCategory(product.getBookCategory());
        dto.setBookDesc(product.getBookDesc());
        dto.setBookImg(product.getBookImg());
        return dto;
    }

    private Product toEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setProductId(dto.getProductId());
        entity.setBookTitle(dto.getBookTitle());
        entity.setBookPrice(dto.getBookPrice());
        entity.setBookQuantity(dto.getBookQuantity());
        entity.setBookCategory(dto.getBookCategory());
        entity.setBookDesc(dto.getBookDesc());
        entity.setBookImg(dto.getBookImg());
        return entity;
    }
}