package com.example.user_verification.repository;

import com.example.user_verification.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository  extends JpaRepository<Brand, Long> {

    Brand findByBrandId(Long brandId);

    boolean existsByCompanyId(Long companyId);

    Page<Brand> findAllByCompanyId(Long companyId, Pageable pageable);
}
