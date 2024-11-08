package com.example.user_verification.repository;

import com.example.user_verification.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByPaymentDetailsId(Long paymentDetailsId);
}
