package com.dhkstnaos.shoeorder.order.service;

import com.dhkstnaos.shoeorder.order.domain.Email;
import com.dhkstnaos.shoeorder.order.domain.Order;
import com.dhkstnaos.shoeorder.order.domain.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);

    List<Order> getAllOrders();
    Order findOrder(UUID orderId);
    void deleteOrder(UUID orderId);

    void deleteAll();
}
