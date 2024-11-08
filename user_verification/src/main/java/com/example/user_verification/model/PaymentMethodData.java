package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentMethodData {

    private String paymentMethodId;
    private String customerId;
    private String paymentMethodType;
    private String customerEmail;
    private String cardBrand;

}
