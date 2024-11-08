package com.example.user_verification.repository;

import com.example.user_verification.model.ChargeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeDetailsRepository extends JpaRepository<ChargeDetails, Long> {
    boolean existsByChargeId(String id);
}
