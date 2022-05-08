package com.dhkstnaos.shoeorder.product.service;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;
import com.dhkstnaos.shoeorder.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class DefaultProductServiceTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    Product newProduct;

    @BeforeEach
    void 초기_세팅() {
        newProduct = productService.createProduct("에어포스1", Category.SNEAKERS, 135000L);
    }

    @AfterEach
    void DB_초기화() {
        productRepository.deleteAll();
    }

    @Test
    void category로_product를_검색한다() {
        List<Product> productByCategory = productService.getProductByCategory(Category.SNEAKERS);
        Assertions.assertThat(productByCategory.size()).isEqualTo(1);
    }

    @Test
    void product를_검색한다() {
        List<Product> productByCategory = productService.getAllProducts();
        Assertions.assertThat(productByCategory.size()).isEqualTo(1);
    }

}