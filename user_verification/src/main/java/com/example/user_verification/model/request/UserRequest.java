package com.example.user_verification.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {


    private Long userId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String industry;
    private String location;
    private String phoneNumber;


}
