package com.bookstore.app.service;

import com.bookstore.app.entity.Product;
import com.bookstore.app.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public Product update(Long id, Product newProduct) {
        return productRepository.findById(id).map(product -> {
            product.setBookTitle(newProduct.getBookTitle());
            product.setBookPrice(newProduct.getBookPrice());
            product.setBookQuantity(newProduct.getBookQuantity());
            product.setBookCategory(newProduct.getBookCategory());
            product.setBookDesc(newProduct.getBookDesc());
            product.setBookImg(newProduct.getBookImg());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
