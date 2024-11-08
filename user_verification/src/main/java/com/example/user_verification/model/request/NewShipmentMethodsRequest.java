package com.example.user_verification.model.request;

import com.example.user_verification.model.ShipmentMethodOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewShipmentMethodsRequest {

    private Long newShipmentMethodId;
    private Long shipmentReferenceId;
    private String trackingId;
    private String shipmentMethodOptions;
    private String promoCode;
    private String service;
    private Long carrierId;
    private String carrierName;
    private String carrierPrice;


}
