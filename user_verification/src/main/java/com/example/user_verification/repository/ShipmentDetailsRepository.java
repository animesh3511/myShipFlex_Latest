package com.example.user_verification.repository;

import com.example.user_verification.model.PaymentDetails;
import com.example.user_verification.model.ShipmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentDetailsRepository extends JpaRepository<ShipmentDetails, Long> {

    ShipmentDetails findByShipmentDetailsId(Long shipmentDetailsId);

    boolean existsByShipmentDetailsId(Long shipmentDetailsId);
}
