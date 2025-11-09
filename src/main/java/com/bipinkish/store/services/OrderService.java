package com.bipinkish.store.services;

import com.bipinkish.store.dto.OrderDto;
import com.bipinkish.store.exceptions.OrderNotFoundException;
import com.bipinkish.store.exceptions.UserNotFoundException;
import com.bipinkish.store.mappers.OrderMapper;
import com.bipinkish.store.mappers.UserMapper;
import com.bipinkish.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    public List<OrderDto> getOrders() {
        var currentUser = userMapper.toEntity(userService.me());
        if (currentUser == null) {
            throw new UserNotFoundException("Please login first, to see the orders!");
        }
        var orders = orderRepository.getOrdersByCustomer(currentUser);
        return orders.stream().map(orderMapper::toOrderDto).toList();
    }

    public OrderDto getOrderById(Long orderId) {
        var order = orderRepository.getOrderById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found for orderId: " + orderId));
        var user = userService.me();
        if(!order.isPlacedBy(user.getId())) {
            throw new AccessDeniedException("You are not allowed to see this order!");
        }
        return orderMapper.toOrderDto(order);
    }
}
