package com.bookstore.app.service;

import com.bookstore.app.config.KafkaConfig;
import com.bookstore.app.dto.ProductDTO;
import com.bookstore.app.entity.Product;
import com.bookstore.app.event.KafkaProducer;
import com.bookstore.app.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService service;

    private Product sampleEntity;
    private ProductDTO sampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEntity = new Product();
        sampleEntity.setProductId(1L);
        sampleEntity.setBookTitle("Book A");
        sampleEntity.setBookPrice(99.99);
        sampleEntity.setBookQuantity(10);
        sampleEntity.setBookCategory("Fiction");
        sampleEntity.setBookDesc("Sample book");
        sampleEntity.setBookImg("img.jpg");

        sampleDTO = new ProductDTO();
        sampleDTO.setProductId(1L);
        sampleDTO.setBookTitle("Book A");
        sampleDTO.setBookPrice(99.99);
        sampleDTO.setBookQuantity(10);
        sampleDTO.setBookCategory("Fiction");
        sampleDTO.setBookDesc("Sample book");
        sampleDTO.setBookImg("img.jpg");
        
    }

    @Test
    void testGetAllProducts() {
        when(repository.findAll()).thenReturn(Flux.just(sampleEntity));

        StepVerifier.create(service.getAllProducts())
                .expectNextMatches(dto -> dto.getBookTitle().equals("Book A"))
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.getProductById(1L))
                .expectNextMatches(dto -> dto.getProductId() == 1L)
                .verifyComplete();
    }

    @Test
    void testCreateProduct() {
        when(repository.save(any(Product.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.createProduct(sampleDTO))
                .expectNextMatches(dto -> dto.getProductId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.PRODUCT_TOPIC), contains("created"));
    }

    @Test
    void testUpdateProduct() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.save(any(Product.class))).thenReturn(Mono.just(sampleEntity));

        StepVerifier.create(service.updateProduct(1L, sampleDTO))
                .expectNextMatches(dto -> dto.getProductId() == 1L)
                .verifyComplete();

        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.PRODUCT_TOPIC), contains("updated"));
    }

    @Test
    void testDeleteProduct() {
        when(repository.findById(1L)).thenReturn(Mono.just(sampleEntity));
        when(repository.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteProduct(1L))
                .verifyComplete();

        verify(repository, times(1)).delete(any());
        verify(kafkaProducer, times(1)).send(eq(KafkaConfig.PRODUCT_TOPIC), contains("deleted"));
    }
}
