package com.bookstore.app.service;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.entity.Product;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final KafkaProducer kafkaProducer;

    @Value("${kafka.topic.product}")
    private String productTopic;

    public Flux<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .map(this::toDTO);
    }

    public Mono<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO);
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto) {
        Product product = toEntity(dto);
        return productRepository.save(product)
                .map(this::toDTO)
                .doOnSuccess(saved -> sendKafkaEvent("created", saved));
    }

    public Mono<ProductDTO> updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id)
                .flatMap(existing -> {
                    existing.setBookTitle(dto.getBookTitle());
                    existing.setBookPrice(dto.getBookPrice());
                    existing.setBookQuantity(dto.getBookQuantity());
                    existing.setBookCategory(dto.getBookCategory());
                    existing.setBookDesc(dto.getBookDesc());
                    existing.setBookImg(dto.getBookImg());
                    return productRepository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("updated", updated));
    }

    public Mono<ProductDTO> updateProductQuantity(Long id, ProductDTO dto) {
        return productRepository.findById(id)
                .flatMap(existing -> {
                    existing.setBookQuantity(dto.getBookQuantity());
                    return productRepository.save(existing);
                })
                .map(this::toDTO)
                .doOnSuccess(updated -> sendKafkaEvent("quantity-updated", updated));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .flatMap(existing ->
                    productRepository.delete(existing)
                        .doOnSuccess(v -> sendKafkaEvent("deleted", toDTO(existing)))
                );
    }

    private void sendKafkaEvent(String action, ProductDTO dto) {
        String message = String.format("{\"event\":\"%s\",\"title\":\"%s\",\"id\":%d}", 
                            action, dto.getBookTitle(), dto.getProductId());
        kafkaProducer.send(productTopic, message);
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