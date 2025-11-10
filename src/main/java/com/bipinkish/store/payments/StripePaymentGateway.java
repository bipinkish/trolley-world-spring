package com.bipinkish.store.payments;

import com.bipinkish.store.entities.Order;
import com.bipinkish.store.entities.OrderItem;
import com.bipinkish.store.entities.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel?orderId=" + order.getId())
                            .setPaymentIntentData(createPaymentIntent(order));

            order.getOrderItems().forEach(orderItem -> {
                var lineItem = createLineItem(orderItem);
                builder.addLineItem(lineItem);
            });
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", order.getId().toString()).build();
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));
                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));
                default -> Optional.empty();
            };
        } catch (SignatureVerificationException e) {
            throw new PaymentException(e.getMessage());
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObj = event.getDataObjectDeserializer().getObject().orElseThrow(() -> new PaymentException("Couldn't deserialize event, Check SDK and API version of stripe."));
        var paymentIntent = (PaymentIntent) stripeObj;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private static SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(orderItem.getQuantity()))
                .setPriceData(createPriceData(orderItem))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData createPriceData(OrderItem orderItem) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmountDecimal(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(100))) // convert ₹ → paise
                .setProductData(createProductData(orderItem))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem orderItem) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(orderItem.getProduct().getName())
                .build();
    }
}
