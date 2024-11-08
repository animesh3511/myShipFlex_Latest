package com.example.user_verification.repository;

import com.example.user_verification.model.CommodityDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommodityDetailsRepository extends JpaRepository<CommodityDetails, Long> {
    boolean existsByCommodityDetailsId(Long commodityId);

    List<CommodityDetails> findByTrackingId(String trackingId);
}
