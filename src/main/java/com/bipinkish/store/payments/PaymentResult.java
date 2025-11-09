package com.bipinkish.store.payments;

import com.bipinkish.store.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private OrderStatus paymentStatus;
}
