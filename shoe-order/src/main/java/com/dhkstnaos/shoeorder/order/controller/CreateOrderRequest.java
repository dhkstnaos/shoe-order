package com.dhkstnaos.shoeorder.order.controller;

import com.dhkstnaos.shoeorder.order.domain.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        String email, String address, String postcode, List<OrderItem> orderItems
) {
}