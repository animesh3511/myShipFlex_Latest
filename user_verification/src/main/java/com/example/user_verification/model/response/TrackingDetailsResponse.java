package com.example.user_verification.model.response;

import com.example.user_verification.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackingDetailsResponse {

    private String trackingId;
    private Long pickup;
    private String carrier;
    private String from;
    private String to;
    private String total;
    private Long boxes;
    private LocalDateTime date;
    private String itemShipped;
    private DeliveryStatus deliveryStatus;
    private String service;
    private String weight;



}
