package com.dhkstnaos.shoeorder.order.repository;

import com.dhkstnaos.shoeorder.order.domain.Email;
import com.dhkstnaos.shoeorder.order.domain.Order;
import com.dhkstnaos.shoeorder.order.domain.OrderItem;
import com.dhkstnaos.shoeorder.order.domain.OrderStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class ProductJdbcRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    Order order, newOrder;

    @BeforeEach
    void 초기_세팅() {
        Product product = new Product(UUID.randomUUID(), "에어포스1", Category.SNEAKERS, 1300L);
        productRepository.insert(product);
        List<OrderItem> items = List.of(
                new OrderItem(product.getProductId(), Category.SNEAKERS, 1300L, 3));
        order = new Order(UUID.randomUUID(), new Email("dhkstnaos@gmail.com"), "동백고", "17003", items, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
        newOrder = orderRepository.insert(order);
    }

    @AfterEach
    void DB_초기화() {
        orderRepository.deleteAll();
    }

    @Test
    void order_객체_저장한다() {
        Assertions.assertThat(newOrder.getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    void order_리스트를_조회한다() {
        List<Order> orders = orderRepository.findAll();
        Assertions.assertThat(orders.get(0).getOrderItems().size()).isEqualTo(1);
    }

    @Test
    void id로_order를_조회한다() {
        Optional<Order> findOrder = orderRepository.findById(order.getOrderId());
        Assertions.assertThat(findOrder.get().getOrderId()).isEqualTo(order.getOrderId());
    }
    
    @Test
    void id로_order_삭제한다() {
        orderRepository.deleteById(order.getOrderId());
    }

}