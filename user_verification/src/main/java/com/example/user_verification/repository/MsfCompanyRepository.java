package com.example.user_verification.repository;

import com.example.user_verification.model.MsfCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MsfCompanyRepository extends JpaRepository<MsfCompany, Long> {

    boolean existsByEmail(String email);
    

    Optional<MsfCompany> findByEmail(String email);

    boolean existsByMobNumber(String mobNumber);

    boolean existsByCompanyId(Long companyId);

    MsfCompany findByCompanyId(Long companyId);

    

  /*  @Query(value = "SELECT * FROM user u WHERE (u.user_name = :name OR :name IS NULL) AND " +
            "(u.email = :email OR :email IS NULL) AND (u.location = :location OR :location IS NULL)", nativeQuery = true)
    List<MsfCompany> findByNameEmailLocation(String name, String email, String location);*/

    //boolean existsByLocation(String location);

    //List<MsfCompany> findByLocation(String location);
}
