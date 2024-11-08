package com.example.user_verification.repository;

import com.example.user_verification.model.CargoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoDetailsRepository extends JpaRepository<CargoDetails, Long> {
    boolean existsByCargoDetailsId(Long cargoDetailsId);

    CargoDetails findByCargoDetailsId(Long cargoDetailsId);

    CargoDetails findByTrackingId(String trackingId);
}
