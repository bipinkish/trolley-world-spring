package com.bipinkish.store.payments;

import com.bipinkish.store.entities.Order;
import com.bipinkish.store.entities.OrderItem;
import com.bipinkish.store.entities.OrderStatus;
import com.bipinkish.store.exceptions.CartItemsNotFoundException;
import com.bipinkish.store.exceptions.CartNotFoundException;
import com.bipinkish.store.exceptions.OrderNotFoundException;
import com.bipinkish.store.mappers.UserMapper;
import com.bipinkish.store.repositories.CartRepository;
import com.bipinkish.store.repositories.OrderRepository;
import com.bipinkish.store.services.CartService;
import com.bipinkish.store.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Value("${websiteUrl}")
    private String websiteUrl;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        var cart = cartRepository.getCartWithItems(checkoutRequest.getCartId()).orElseThrow(CartNotFoundException::new);
        if (cart.getCartItems().isEmpty()) {
            throw new CartItemsNotFoundException();
        }
        // GET THE LOGGED-IN USER
        var currentUser = userMapper.toEntity(userService.me());
        // CREATE THE NEW ORDER WITH CART DETAILS AND MARK AS PENDING
        var newOrder = Order.builder()
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.PENDING)
                .customer(currentUser)
                .build();

        // CREATE ORDER ITEMS FROM THE CART LIST AND SAVE TO DB
        cart.getCartItems().forEach(item -> {
            var orderItem = OrderItem.builder()
                    .order(newOrder)
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getProduct().getPrice())
                    .totalPrice(item.getTotalPrice())
                    .build();
            newOrder.getOrderItems().add(orderItem);
        });

        orderRepository.save(newOrder);

        // AFTER SAVED AS PENDING, CREATE THE CHECKOUT SESSION
        try {
            var session = paymentGateway.createCheckoutSession(newOrder);
            // DELETE THE CART ITEMS FROM THE CART ONCE CHECKED OUT
            cartService.deleteCartItems(cart.getId());
            return new CheckoutResponse(newOrder.getId(), session.getCheckoutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(newOrder);
            throw ex;
        }
    }

    public void handleWebhook(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
