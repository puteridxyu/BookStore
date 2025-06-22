package com.bookstore.app.service;

import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.entity.Product;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService productService;

    private Product sampleEntity;
    private ProductDTO sampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(productService, "productTopic", "product-topic");

        sampleEntity = new Product();
        sampleEntity.setProductId(1L);
        sampleEntity.setBookTitle("Sample Book");
        sampleEntity.setBookPrice(50.0);
        sampleEntity.setBookQuantity(10);
        sampleEntity.setBookCategory("Fiction");
        sampleEntity.setBookDesc("A sample description");
        sampleEntity.setBookImg("cover.jpg");

        sampleDTO = new ProductDTO();
        sampleDTO.setProductId(1L);
        sampleDTO.setBookTitle("Sample Book");
        sampleDTO.setBookPrice(50.0);
        sampleDTO.setBookQuantity(10);
        sampleDTO.setBookCategory("Fiction");
        sampleDTO.setBookDesc("A sample description");
        sampleDTO.setBookImg("cover.jpg");
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(productService.getAllProducts())
                .expectNextMatches(dto -> dto.getBookTitle().equals("Sample Book"))
                .verifyComplete();
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(productService.getProductById(1L))
                .expectNextMatches(dto -> dto.getProductId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any())).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(productService.createProduct(sampleDTO))
                .expectNextMatches(dto -> dto.getBookTitle().equals("Sample Book"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("product-topic"), contains("created"));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(productRepository.save(any())).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(productService.updateProduct(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getBookTitle().equals("Sample Book"))
                .verifyComplete();

        verify(kafkaProducer).send(eq("product-topic"), contains("updated"));
    }

    @Test
    void testUpdateProductQuantity() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(productRepository.save(any())).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(productService.updateProductQuantity(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getBookQuantity() == 10)
                .verifyComplete();

        verify(kafkaProducer).send(eq("product-topic"), contains("quantity-updated"));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(productRepository.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProduct(1L))
                .verifyComplete();

        verify(kafkaProducer).send(eq("product-topic"), contains("deleted"));
    }
}
