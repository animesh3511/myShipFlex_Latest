package com.example.user_verification.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalShipmentPaymentDetailsRequest {

    private Long shipmentDetailsId;
    private String paymentMethod;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private String cvv;
    private String cardType;
    private String monthYear;

}
