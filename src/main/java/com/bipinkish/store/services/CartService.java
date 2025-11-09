package com.bipinkish.store.services;

import com.bipinkish.store.dto.CartDto;
import com.bipinkish.store.dto.CartItemDto;
import com.bipinkish.store.entities.Cart;
import com.bipinkish.store.exceptions.CartNotFoundException;
import com.bipinkish.store.exceptions.ProductNotFoundException;
import com.bipinkish.store.mappers.CartMapper;
import com.bipinkish.store.repositories.CartRepository;
import com.bipinkish.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart() {
        var cart = new Cart();
        return cartMapper.toDto(cartRepository.save(cart));
    }

    public CartItemDto addItemToCart(UUID cartId, Long productId) {
        var cart = findCartWithItems(cartId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var selectedProduct = productRepository.findById(productId).orElse(null);
        if (selectedProduct == null) {
            throw new ProductNotFoundException();
        }
        var cartItem = cart.addItem(selectedProduct);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = findCartWithItems(cartId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity) {
        var cart = findCartWithItems(cartId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = findCartWithItems(cartId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var deletedCartItem = cart.removeItem(productId);
        cartRepository.save(cart);
        if (deletedCartItem == null) {
            throw new ProductNotFoundException();
        }
    }

    public void deleteCartItems(UUID cartId) {
        var cart = findCartWithItems(cartId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clearCart();
        cartRepository.save(cart);
    }

    private Cart findCartWithItems(UUID id) {
        return cartRepository.getCartWithItems(id).orElse(null);
    }
}
