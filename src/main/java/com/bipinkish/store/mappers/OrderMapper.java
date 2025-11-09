package com.bipinkish.store.mappers;

import com.bipinkish.store.dto.OrderDto;
import com.bipinkish.store.dto.OrderItemDto;
import com.bipinkish.store.entities.Order;
import com.bipinkish.store.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toOrderDto(Order order);

    @Mapping(source = "product.id", target = "product.id")
    @Mapping(source = "product.name", target = "product.name")
    @Mapping(source = "unitPrice", target = "product.price")
    OrderItemDto  toOrderItemDto(OrderItem orderItem);
}
