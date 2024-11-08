package com.example.user_verification.repository;

import com.example.user_verification.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {



    boolean existsByCompanyId(Long companyId);

    Token findByCompanyId(Long companyId);

    //String getTokenByCompanyId(Long companyId);

  /*  @Query(value = "select * from token order by updated_at desc limit 1 ", nativeQuery = true)
    Token findAllByUpdatedAt();


    boolean existsByToken(String token);

    @Query(value = "SELECT * FROM token t ORDER BY t.updated_at DESC limit 1", nativeQuery = true)
    Token findLatestToken();*/
}
