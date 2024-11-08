package com.example.user_verification.model.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDetailsRequest {

    private Long paymentDetailsId;
    private String paymentMethod;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private String cvv;
    private String cardType;
    private String monthYear;

}
