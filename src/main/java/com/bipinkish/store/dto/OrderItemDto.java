package com.bipinkish.store.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;


    @Data
    public static class OrderProductDto {
        private Long id;
        private String name;
        private BigDecimal price;
    }

}


