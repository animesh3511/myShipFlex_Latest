package com.example.user_verification.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailsRequest {

    private Long shipmentDetailsId;
    private String hsnCode;
    private String itemName;
    private String length;
    private String width;
    private String height;
    private String itemVolumetricWeight;
    private String itemPhysicalWeight;

}
