package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.AddItemToCartRequest;
import com.bipinkish.store.dto.CartDto;
import com.bipinkish.store.dto.CartItemDto;
import com.bipinkish.store.dto.UpdateItemToCartRequest;
import com.bipinkish.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @Operation(summary = "Create a new cart")
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var newCart = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").build(newCart.getId());
        return ResponseEntity.created(uri).body(newCart);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addItemToCart(@PathVariable UUID cartId, @Valid @RequestBody AddItemToCartRequest request) {
        var newCartItem = cartService.addItemToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newCartItem);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@Parameter(description = "Unique Cart ID that you want to fetch")  @PathVariable UUID cartId) {
        var cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateItemToCartRequest request) {
        var updatedCart = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> deleteCartItems(@PathVariable UUID cartId) {
        cartService.deleteCartItems(cartId);
        return ResponseEntity.noContent().build();
    }

}
