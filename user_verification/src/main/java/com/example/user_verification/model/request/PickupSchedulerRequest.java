package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PickupSchedulerRequest {

    private Long pickupSchedulerId;
    private Long shipmentReferenceId;
    private String trackingId;
    private String country;
    private String companyName;
    private String contactName;
    private String address;
    private String address2;
    private String city;
    private String zipcode;
    private String state;
    private String phoneNumber;
    private String email;
    private LocalDateTime pickUpDate;
    private LocalTime earliestPickUpTime;
    private LocalTime latestPickUpTime;

}
