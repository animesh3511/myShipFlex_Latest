package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InsuranceRequest {

    private Long insuranceId;
    private Long shipmentReferenceId;
    private String trackingId;
    private String insureFreight;

}
