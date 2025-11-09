package com.bipinkish.store.mappers;

import com.bipinkish.store.dto.CartDto;
import com.bipinkish.store.dto.CartItemDto;
import com.bipinkish.store.entities.Cart;
import com.bipinkish.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
