package com.dhkstnaos.shoeorder.product.service;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;
import com.dhkstnaos.shoeorder.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        Product product = new Product(UUID.randomUUID(), productName, category, price);
        return productRepository.insert(product);
    }

    @Override
    public Product createProduct(String productName, Category category, long price, String description) {
        Product product = new Product(UUID.randomUUID(), productName, category, price, description);
        return productRepository.insert(product);
    }

    @Override
    public Product findProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.update(product);
    }

    @Override
    public void deleteById(UUID productId) {
        productRepository.deleteById(productId);
    }
}
