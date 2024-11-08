package com.example.user_verification.mapper;

import com.example.user_verification.model.MsfCompany;
import org.springframework.stereotype.Component;

@Component
public class MsfCompanyViewMapper {


    public MsfCompanyView toMsfCompanyView(MsfCompany msfCompany) {

        MsfCompanyView msfCompanyView = new MsfCompanyView();
        msfCompanyView.setId(msfCompany.getId());
        msfCompanyView.setAccountType(msfCompany.getAccountType());
        msfCompanyView.setCompanyId(msfCompany.getCompanyId());
        msfCompanyView.setCreatedAt(msfCompany.getCreatedAt());
        msfCompanyView.setEmail(msfCompany.getEmail());
        msfCompanyView.setIsActive(msfCompany.getIsActive());
        msfCompanyView.setIsDeleted(msfCompany.getIsDeleted());
        msfCompanyView.setMobNumber(msfCompany.getMobNumber());
        msfCompanyView.setLocation(msfCompany.getLocation());
        msfCompanyView.setName(msfCompany.getName());
        msfCompanyView.setPassword(msfCompany.getPassword());
        msfCompanyView.setUpdatedAt(msfCompany.getUpdatedAt());
        return msfCompanyView;

    }

}
