package com.example.user_verification.controller;

import com.example.user_verification.model.CustomerData;
import com.example.user_verification.model.request.ChargeRequest;
import com.example.user_verification.model.request.Response;
import com.example.user_verification.repository.CustomerDataRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentIntentController {

    @Autowired
    CustomerDataRepository customerDataRepository;


    @PostMapping("/create-payment-intent")
    public Response createPaymentIntent(@RequestBody ChargeRequest chargeRequest)
            throws StripeException {

        String email = chargeRequest.getEmail();
        CustomerData customerData = customerDataRepository.findByEmail(email);
        String customerId = customerData.getCustomerId();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(chargeRequest.getAmount() * 100L)
                        .setCustomer(customerId)
                        .putMetadata("productName",
                                chargeRequest.getProductName())
                        .setCurrency("usd")
                        //.setPaymentMethod("card")
                        .build();

        PaymentIntent intent =
                PaymentIntent.create(params);
        return new Response(intent.getId(),
                intent.getClientSecret());
    }


    @PostMapping("/createPaymentIntent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, String> data) throws StripeException {

        System.out.println("inside payment intent");
        String email = data.get("email");
        CustomerData customerData = customerDataRepository.findByEmail(email);
        String customerId = customerData.getCustomerId();
        Stripe.apiKey = "sk_test_51Py4ib082jp7x4Erpgw4fq75qOFbN1JwYFwh0TzHQiG4xsgmivrcBmm74p3tL40yFCqSlziILIJbV7Ore5AHzlIS00TIVe1rGs";

        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        // params.put("payment_method_types[]", "card");
        params.put("payment_method_types", Arrays.asList("card"));
        params.put("amount", 100); // Set amount to 0 for attaching card
        params.put("currency", "usd");
        PaymentIntent intent = PaymentIntent.create(params);
        Map<String, String> response = new HashMap<>();
        response.put("id", intent.getId());
        response.put("client_secret", intent.getClientSecret());
        return ResponseEntity.ok(response);
    }

}
