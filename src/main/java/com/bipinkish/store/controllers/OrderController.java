package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.OrderDto;
import com.bipinkish.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        var orders = orderService.getOrders();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long orderId) {
        var order = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }
}
