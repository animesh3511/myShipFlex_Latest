package com.example.user_verification.repository;

import com.example.user_verification.model.CardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardDataRepository extends JpaRepository<CardData, Long> {


    boolean existsByFingerPrintAndCustomerId(String fingerprint, String customerId);

    CardData findByCustomerId(String customerId);

    @Query(value = "select * from card_data where last4 = :cardLast41 AND customer_id = :customerId", nativeQuery = true)
    CardData findByLast4AndCustomerId(String cardLast41, String customerId);
}
