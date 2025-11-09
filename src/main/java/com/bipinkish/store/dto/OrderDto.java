package com.bipinkish.store.dto;

import com.bipinkish.store.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private Set<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
}
