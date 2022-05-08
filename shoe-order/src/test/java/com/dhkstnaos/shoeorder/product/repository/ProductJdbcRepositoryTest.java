package com.dhkstnaos.shoeorder.product.repository;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class ProductJdbcRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    Product product, newProduct;
    @BeforeEach
    void 초기_세팅() {
        product = new Product(UUID.randomUUID(), "에어포스1", Category.SNEAKERS, 135000L);
        newProduct = productRepository.insert(product);
    }

    @AfterEach
    void DB_초기화() {
        productRepository.deleteAll();
    }

    @Test
    void Product_객체_저장한다() {
        Assertions.assertThat(newProduct.getProductId()).isEqualTo(product.getProductId());
    }

    @Test
    void Product_객체_리스트_반환한다() {
        List<Product> all = productRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void Product_객체_업데이트한다() {
        String change = "변경된 값";
        product.setProductName(change);
        Product update = productRepository.update(product);
        Assertions.assertThat(update.getProductName()).isEqualTo(change);
    }

    @Test
    void ID로_Product_객체_검색한다() {
        Optional<Product> findProduct = productRepository.findById(product.getProductId());
        Assertions.assertThat(findProduct.get().getProductId()).isEqualTo(product.getProductId());
    }

    @Test
    void productName으로_Product_객체_검색한다() {
        Optional<Product> findProduct = productRepository.findByName(product.getProductName());
        Assertions.assertThat(findProduct.get().getProductId()).isEqualTo(product.getProductId());
    }

    @Test
    void category로_Product_객체_검색한다() {
        List<Product> findProducts = productRepository.findByCategory(product.getCategory());
        Assertions.assertThat(findProducts.size()).isEqualTo(1);
    }
}