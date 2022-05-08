package com.dhkstnaos.shoeorder.order.domain;

import com.dhkstnaos.shoeorder.product.domain.Category;

import java.util.UUID;

public record OrderItem(UUID productId, Category category, long price, int quantity) {
}