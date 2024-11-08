package com.example.user_verification.repository;

import com.example.user_verification.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCompanyId(Long companyId);

    List<User> findAllByCompanyId(Long companyId, Pageable pageable);

    boolean existsByPhoneNumberAndUserIdNotIn(String phoneNumber, List<Long> userId);
}
