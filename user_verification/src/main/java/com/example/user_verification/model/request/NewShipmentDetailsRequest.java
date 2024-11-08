package com.example.user_verification.model.request;

import com.example.user_verification.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewShipmentDetailsRequest {

    private Long id;
    private String country;
    private String companyName;
    private String contactPersonName;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
    private String phoneNo;
    private String email;
    private String deliveryCountry;
    private String deliveryCompanyName;
    private String deliveryContactPersonName;
    private String deliveryAddress;
    private String deliveryAddress2;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZipcode;
    private String deliveryPhoneNo;
    private String deliveryEmail;
    private DeliveryStatus deliveryStatus;
    private String pendingReason;

}
