package com.example.user_verification.repository;

import com.example.user_verification.model.NewShipmentMethods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewShipmentMethodsRepository extends JpaRepository<NewShipmentMethods, Long> {
    boolean existsByNewShipmentMethodId(Long newShipmentMethodId);

    NewShipmentMethods findByNewShipmentMethodId(Long newShipmentMethodId);

    NewShipmentMethods findByTrackingId(String trackingId);
}
