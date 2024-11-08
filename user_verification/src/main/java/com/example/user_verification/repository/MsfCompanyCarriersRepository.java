package com.example.user_verification.repository;

import com.example.user_verification.model.MsfCompanyCarriers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MsfCompanyCarriersRepository extends JpaRepository<MsfCompanyCarriers, Long> {

    boolean existsByCompanyId(Long companyId);

    @Query(value = "SELECT m.carrier_id AS carrierId, m.company_id AS companyId," +
            " c.name AS carrierName FROM msf_company_carriers m INNER JOIN carrier1 c " +
            "ON m.carrier_id = c.carrier_id WHERE m.company_id = :companyId", nativeQuery = true)
    List<Object> findAllByComapnyId(Long companyId);
}
