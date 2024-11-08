package com.example.user_verification.repository;

import com.example.user_verification.model.CommercialInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommercialInvoiceRepository extends JpaRepository<CommercialInvoice, Long> {
    boolean existsByCommercialInvoiceId(Long commercialInvoiceId);

    CommercialInvoice findByTrackingId(String trackingId);
}
