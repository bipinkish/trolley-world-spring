package com.bipinkish.store.repositories;

import com.bipinkish.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}