package com.example.user_verification.service;

public interface TokenService {

    Object saveToken(Long companyID, String token);

    Object getTokenByCompanyId(Long companyId);

}
