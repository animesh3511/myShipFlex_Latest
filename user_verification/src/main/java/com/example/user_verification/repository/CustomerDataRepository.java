package com.example.user_verification.repository;

import com.example.user_verification.model.CustomerData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDataRepository extends JpaRepository<CustomerData, Long> {


    CustomerData findByEmail(String email);

    CustomerData findByCustomerId(String customerId);

    boolean existsByCustomerId(String customerId);
}
