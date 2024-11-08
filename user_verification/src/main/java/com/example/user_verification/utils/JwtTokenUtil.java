package com.example.user_verification.utils;

import com.example.user_verification.model.Token;
import com.example.user_verification.repository.MsfCompanyRepository;
import com.example.user_verification.repository.TokenRepository;
import com.example.user_verification.service.TokenService;
import com.example.user_verification.serviceimpl.UserToken;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    // @Autowired
    //  private TokenCache tokenCache;
    @Autowired
    private UserToken userToken;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MsfCompanyRepository msfCompanyRepository;
    @Autowired
    private TokenService tokenService;
    @Value("${jwt.expiration}")
    private int expiration;


    private static final String SECRET_KEY = "hdfheyritkvmvnbdgaqworuthgjbvgftredswqazsxcdrtgyhujioplkmnjhgtrferdesdfvbgfdswqazxcdrtyuioplkjnhbgvfddcdjhgythdbnvjioapskdjetcvdbdg";

    public JwtTokenUtil() throws Exception {
    }

    public String generateToken(String email) throws Exception {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        // tokenCache.put(userToken.getCompanyFromToken().getCompanyId(), token);
        return token;

    }

    public boolean validate(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            System.out.println("Token validation successful for subject: " + claimsJws.getBody().getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }


    public String getEmail(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

//class ends here
}
