package com.example.user_verification.filter;

import com.example.user_verification.model.MsfCompany;
import com.example.user_verification.model.Token;
import com.example.user_verification.repository.MsfCompanyRepository;
import com.example.user_verification.repository.TokenRepository;
import com.example.user_verification.service.TokenService;
import com.example.user_verification.serviceimpl.UserDetailsImpl;
import com.example.user_verification.serviceimpl.UserToken;
import com.example.user_verification.utils.JwtTokenUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Component
@Getter
@Setter
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    private MsfCompanyRepository msfCompanyRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TokenRepository tokenRepository;
    //@Autowired
    // private TokenCache tokenCache;
    @Autowired
    private UserToken userToken;
    @Autowired
    private TokenService tokenService;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Token sent from request's header " + header);

        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();

        //tokenService.saveToken(userToken.getCompanyFromToken().getCompanyId(), token);

        //String latestToken = tokenCache.get(companyId);
        // System.out.println("LatestToken " + latestToken);

        /*if (!token.equals(latestToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("UNAUTHORIZED");
            return;
        }*/
/*

        String latestToken = tokenRepository.getTokenByCompanyId(userToken.getCompanyFromToken().getCompanyId());

        if (!token.equals(latestToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
*/

        if (!jwtTokenUtil.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtTokenUtil.getEmail(token);
        MsfCompany msfCompany = msfCompanyRepository.findByEmail(email).orElseThrow(() -> new Exception("Email does not exists"));
        UserDetails userDetails = UserDetailsImpl.build(msfCompany);

/*
         if (tokenRepository.existsByCompanyId(msfCompany.getCompanyId())) {
                 Token token1 = tokenRepository.findByCompanyId(msfCompany.getCompanyId());
                 token1.setToken(token);
                 tokenRepository.save(token1);
             } else {
                 tokenService.saveToken(msfCompany.getCompanyId(), token);
        }

        Token retrievedToken = tokenRepository.findByCompanyId(msfCompany.getCompanyId());
        String latestToken = retrievedToken.getToken();
        if (!token.equals(latestToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("UNAUTHORIZED");
            return;
        }*/


        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        Arrays.asList() : userDetails.getAuthorities()

        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
        System.out.println("Authorities: " + authentication);


        if (tokenRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            Token token1 = tokenRepository.findByCompanyId(userToken.getCompanyFromToken().getCompanyId());
            token1.setToken(token);
            tokenRepository.save(token1);
        } else {
            tokenService.saveToken(userToken.getCompanyFromToken().getCompanyId(), token);
        }

    }
//class ends here
}
