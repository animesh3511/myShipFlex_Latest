package com.example.user_verification.model.response;

import com.example.user_verification.model.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FilterResponse {

    public FilterResponse(String trackingId, LocalDateTime Date, DeliveryStatus Status,
                          String from, String to, String carrier,
                          String service, String total, String itemsShipped, String weight) {
        this.trackingId = trackingId;
        this.Date = Date;
        this.Status = Status;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.service = service;
        this.total = total;
        this.itemsShipped = itemsShipped;
        this.weight = weight;
    }

    private String trackingId;
    private LocalDateTime Date;
    private DeliveryStatus Status;
    private String from;
    private String to;
    private String carrier;
    private String service;
    private String total;
    private String itemsShipped;
    private String weight;
    private Long boxes;

}
