package com.example.user_verification.model.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProfileDetailsRequest {

    private Long shipmentDetailsId;
    private String profileFirstName;
    private String profileLastName;
    private String emailId;
    private String phoneNo;
    private String address;
    private String address1;
    private String postalCode;
    private String location;

}
