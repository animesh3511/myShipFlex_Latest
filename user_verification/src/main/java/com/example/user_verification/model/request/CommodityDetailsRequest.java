package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommodityDetailsRequest {

    private Long commodityDetailsId;
    private Long shipmentReferenceId;
    private String trackingId;
    private String itemName;
    private String units;
    private String hsCode;
    private String unitPrice;
    private String unitWeight;
    private String ofBoxesContainIt;

}
