package com.dhkstnaos.shoeorder.order.controller;

import com.dhkstnaos.shoeorder.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String ordersPage(Model model) {
        var orders=orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @DeleteMapping("/order/{orderId}")
    public String deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(UUID.fromString(orderId));
        return "redirect:/orders";
    }
}
