package com.example.user_verification.repository;

import com.example.user_verification.model.StoreDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreDetailsRepository extends JpaRepository<StoreDetails, Long> {


    boolean existsByCompanyId(Long companyId);

    StoreDetails findByCompanyId(Long companyId);
}
