package com.bipinkish.store.payments;

import com.bipinkish.store.repositories.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody @Valid CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.checkout(checkoutRequest));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
       checkoutService.handleWebhook(new WebhookRequest(headers, payload));
       return ResponseEntity.ok().build();
    }

}
