package com.example.user_verification.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductDeliveryRequest {

    private Long shipmentDetailsId;
    private String deliveryType;
    private Long carrierId;
    private String carrierName;
    private String carrierMarkupPercentage;
    private MultipartFile deliveryDocument;

}
