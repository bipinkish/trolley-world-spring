package com.bipinkish.store.payments;

public class PaymentException extends RuntimeException{
    public PaymentException(String message) {
        super(message);
    }
}
