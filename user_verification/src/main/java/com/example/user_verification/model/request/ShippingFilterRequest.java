package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingFilterRequest {

    private String shippingMethod;
    private String carrier;
    private String fromCountry;
    private String toCountry;
    private String fromDate;
    private String toDate;
    private String pendingReason;
    private String trackingStatus;
    private String sortBy;
    private String sortDirection;

}
