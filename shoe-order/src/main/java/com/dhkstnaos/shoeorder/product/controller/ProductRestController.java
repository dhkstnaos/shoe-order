package com.dhkstnaos.shoeorder.product.controller;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;
import com.dhkstnaos.shoeorder.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/product")
    public List<Product> productList(@RequestParam Optional<Category> category) {
        return category
                .map(productService::getProductByCategory)
                .orElse(productService.getAllProducts());
    }

    @GetMapping("/api/v1/product/{productId}")
    public Product product(@PathVariable String productId) {
        return productService.findProduct(UUID.fromString(productId));
    }
}
