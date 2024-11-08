package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MsfCompanyRequest {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String location;
    private String mobNumber;


}
