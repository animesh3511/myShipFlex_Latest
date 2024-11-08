package com.example.user_verification.model.request;

import com.example.user_verification.model.ShipmentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentDetailsRequest {


    private Long shipmentDetailsId;
    private ShipmentType shipmentType;
    private String pickupName;
    private String pickupEmail;
    private String pickupPhone;
    private String pickupAddress1;
    private String pickupAddress2;
    private String pickupCountry;
    private String pickupZipcode;
    private String pickupCity;
    private String pickupState;
    private String deliveryName;
    private String deliveryEmail;
    private String deliveryPhone;
    private String deliveryAddress1;
    private String deliveryAddress2;
    private String deliveryCountry;
    private String deliveryZipcode;
    private String deliveryCity;
    private String deliveryState;
    private String pendingReason;


}
