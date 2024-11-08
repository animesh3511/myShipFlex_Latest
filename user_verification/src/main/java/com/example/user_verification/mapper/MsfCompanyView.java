package com.example.user_verification.mapper;

import com.example.user_verification.utils.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MsfCompanyView {


    private Long Id;
    private String name;
    private String email;
    private String password;
    private String location;
    private String mobNumber;
    private Long companyId;
    private AccountType accountType;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
