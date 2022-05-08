package com.dhkstnaos.shoeorder.product.controller;

import com.dhkstnaos.shoeorder.product.domain.Category;

public record CreateProductRequest(
        String productName,
        Category category,
        long price,
        String description) {
}
