package com.bipinkish.store.repositories;

import com.bipinkish.store.entities.Order;
import com.bipinkish.store.entities.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems.product")
    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "orderItems.product")
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> getOrderById(@Param("orderId") Long orderId);
}