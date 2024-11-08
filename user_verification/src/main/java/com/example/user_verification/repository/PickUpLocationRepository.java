package com.example.user_verification.repository;

import com.example.user_verification.model.PickUpLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickUpLocationRepository extends JpaRepository<PickUpLocation, Long> {
    PickUpLocation findByPickUpLocationId(Long pickUpLocationId);

    boolean existsByCompanyId(Long companyId);

    Page<PickUpLocation> findAllByCompanyId(Long companyId, Pageable pageable);
}
