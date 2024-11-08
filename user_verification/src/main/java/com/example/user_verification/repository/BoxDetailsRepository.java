package com.example.user_verification.repository;

import com.example.user_verification.model.BoxDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoxDetailsRepository extends JpaRepository<BoxDetails, Long> {
   /* List<BoxDetails> findByTrackingId(String trackingId);*/

    List<BoxDetails> findAllByTrackingId(String trackingId);

    @Query(value = "select count(*) from box_details where tracking_id = :trackingId AND item_name = :itemName", nativeQuery = true)
    Long getCountOfBoxDetailsByTrackingId(String trackingId, String itemName);

    @Query(value = "select count(*) from box_details where tracking_id = :trackingId", nativeQuery = true)
    Long getCountOfBoxes(String trackingId);
}
