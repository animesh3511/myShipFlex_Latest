package com.example.user_verification.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduledPickupResponse {

  private String pickupNumber;
  private Long shipmentReferenceId;
  private String carrier;
  private String pickupAddress;
  private int Boxes;
  private LocalDateTime pickupOn;
  private LocalDateTime createdOn;
  private String companyName;
  private String country;
  private String address;
  private String company;
  private String contactName;
  private String phoneNumber;
  private String email;
  private LocalDateTime pickupDate;
  private String quantity;
  private LocalTime earliestPickupTime;
  private LocalTime latestPickupTime;
  private String boxWeight;

}

