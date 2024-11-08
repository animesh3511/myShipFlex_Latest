package com.example.user_verification.serviceimpl;

import com.example.user_verification.model.MsfCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserToken {

    @Autowired
    private MsfCompanyServiceImpl msfCompanyService;

    public MsfCompany getCompanyFromToken() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            //System.out.println("Email of method userDetails.getUsername: " + email);
            return msfCompanyService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return msfCompanyService.findByEmail("salunkheaparnaoms@gmail.com");
        }
    }
}
