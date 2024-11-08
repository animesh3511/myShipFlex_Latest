package com.example.user_verification.repository;

import com.example.user_verification.model.PickupScheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickupSchedulerRepository extends JpaRepository<PickupScheduler, Long> {
    boolean existsByPickupSchedulerId(Long pickupSchedulerId);

    PickupScheduler findByPickupSchedulerId(Long pickupSchedulerId);

    PickupScheduler findByTrackingId(String trackingId);
}
