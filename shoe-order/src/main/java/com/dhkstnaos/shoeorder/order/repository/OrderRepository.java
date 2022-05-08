package com.dhkstnaos.shoeorder.order.repository;

import com.dhkstnaos.shoeorder.order.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    List<Order> findAll();
    Optional<Order> findById(UUID orderId);
    Order update(Order order);

    void deleteById(UUID orderId);

    void deleteAll();
}
