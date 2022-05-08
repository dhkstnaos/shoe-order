package com.dhkstnaos.shoeorder.product.service;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getProductByCategory(Category category);

    List<Product> getAllProducts();

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);

    Product findProduct(UUID productId);

    Product updateProduct(Product product);

    void deleteById(UUID fromString);
}
