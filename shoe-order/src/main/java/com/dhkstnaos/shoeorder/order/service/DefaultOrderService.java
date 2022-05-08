package com.dhkstnaos.shoeorder.order.service;

import com.dhkstnaos.shoeorder.order.domain.Email;
import com.dhkstnaos.shoeorder.order.domain.Order;
import com.dhkstnaos.shoeorder.order.domain.OrderItem;
import com.dhkstnaos.shoeorder.order.domain.OrderStatus;
import com.dhkstnaos.shoeorder.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService {
    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(
                UUID.randomUUID(),
                email,
                address,
                postcode,
                orderItems,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now());
        return orderRepository.insert(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }
}
