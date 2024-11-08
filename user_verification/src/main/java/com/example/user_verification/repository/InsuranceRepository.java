package com.example.user_verification.repository;

import com.example.user_verification.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {


    boolean existsByInsuranceId(Long insuranceId);

    Insurance findByInsuranceId(Long insuranceId);

    Insurance findByTrackingId(String trackingId);
}
