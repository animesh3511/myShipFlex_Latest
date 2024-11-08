package com.example.user_verification.controller;

import com.example.user_verification.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {


    private final PaypalService paypalService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment(@RequestParam("method") String method,
                                      @RequestParam("amount") String amount,
                                      @RequestParam("currency") String currency,
                                      @RequestParam("description") String description) {

        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(Double.valueOf(amount), currency, method, "sale",
                    description, cancelUrl, successUrl);

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    String href = links.getHref();
                    String decodedHref = URLDecoder.decode(href, StandardCharsets.UTF_8.toString());
                    System.out.println("decoded href: " + decodedHref);
                    return new RedirectView(decodedHref);
                }
            }
        } catch (PayPalRESTException | UnsupportedEncodingException e) {
            log.error("Error occured: ", e);
        }
        return new RedirectView("/payment/error");
    }


    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
            log.error("Error occured: ", e);
        }
        return "paymentSuccess";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }

}
