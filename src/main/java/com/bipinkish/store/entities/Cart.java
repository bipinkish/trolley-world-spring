package com.bipinkish.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.MERGE)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return cartItems.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getCartItem(Long productId) {
        return cartItems.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = getCartItem(product.getId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }
        return cartItem;
    }

    public CartItem removeItem(Long productId) {
        var cartItem = getCartItem(productId);
        if (cartItem != null) {
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
        return cartItem;
    }

    public void clearCart() {
        cartItems.clear();
    }

}