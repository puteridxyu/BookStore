package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.entity.Product;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final KafkaProducer kafkaProducer;

    public Flux<ProductDTO> getAllProducts() {
    	log.info("Fetching all products from DB");
        return repository.findAll().map(this::toDTO);
    }

    @Cacheable(cacheNames = "products", key = "#id")
    public Mono<ProductDTO> getProductById(Long id) {
    	log.info("Fetching product with ID: {} from DB", id);
        return repository.findById(id).map(this::toDTO);
    }

    @CacheEvict(cacheNames = "products", key = "#result.productId", condition = "#result != null")
    public Mono<ProductDTO> createProduct(ProductDTO dto) {
        Product entity = toEntity(dto);
        return repository.save(entity)
                .map(this::toDTO)
                .doOnSuccess(saved -> sendKafkaEvent("created", saved));
    }

    @CacheEvict(cacheNames = "products", key = "#id")
    public Mono<ProductDTO> updateProduct(Long id, ProductDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setBookTitle(dto.getBookTitle());
                    existing.setBookPrice(dto.getBookPrice());
                    existing.setBookQuantity(dto.getBookQuantity());
                    existing.setBookCategory(dto.getBookCategory());
                    existing.setBookDesc(dto.getBookDesc());
                    existing.setBookImg(dto.getBookImg());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("updated", updated));
    }

    @CacheEvict(cacheNames = "products", key = "#id")
    public Mono<ProductDTO> updateProductQuantity(Long id, ProductDTO dto) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setBookQuantity(dto.getBookQuantity());
                    return repository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("quantity-updated", updated));
    }

    @CacheEvict(cacheNames = "products", key = "#id")
    public Mono<Void> deleteProduct(Long id) {
        return repository.findById(id)
                .flatMap(existing ->
                    repository.delete(existing)
                        .doOnSuccess(v -> sendKafkaEvent("deleted", toDTO(existing)))
                );
    }

    private void sendKafkaEvent(String action, ProductDTO dto) {
        String message = String.format(
            "{\"event\":\"%s\",\"title\":\"%s\",\"id\":%d,\"timestamp\":\"%s\"}",
            action, dto.getBookTitle(), dto.getProductId(), java.time.Instant.now()
        );
        kafkaProducer.send(KafkaConfig.PRODUCT_TOPIC, message);
        log.info("Kafka event sent: {}", message);
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
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setBookTitle(dto.getBookTitle());
        product.setBookPrice(dto.getBookPrice());
        product.setBookQuantity(dto.getBookQuantity());
        product.setBookCategory(dto.getBookCategory());
        product.setBookDesc(dto.getBookDesc());
        product.setBookImg(dto.getBookImg());
        return product;
    }
}