package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PickUpLocationRequest {

    private Long pickUpLocationId;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String companyName;
    private String eoriNumber;
    private String vatNumber;
    private String lossNumber;
    private String fullName;
    private String initial;
    private MultipartFile signature;

}
