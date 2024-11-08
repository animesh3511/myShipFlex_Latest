
package com.example.user_verification.configuration;

import com.example.user_verification.filter.JwtTokenFilter;
import com.example.user_verification.model.MsfCompany;
import com.example.user_verification.repository.MsfCompanyRepository;
import com.example.user_verification.serviceimpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private MsfCompanyRepository msfCompanyRepository;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(email -> {
            MsfCompany msfCompany = msfCompanyRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email does not exists for email: " + email));
            UserDetails userDetails = UserDetailsImpl.build(msfCompany);
            return userDetails;
        }).passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http = http.cors().and().csrf().disable();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                })
                .and();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/signUp").permitAll()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/verifyOtpWithLogin").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/payment/create").permitAll()
                .antMatchers("/payment/success").permitAll()
                .antMatchers("/payment/cancel").permitAll()
                .antMatchers("/payment/error").permitAll()
                .antMatchers("/create-payment-intent").permitAll()
                .antMatchers("/createPaymentIntent").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/checkout").permitAll()
                .antMatchers("/index1").permitAll()
                .antMatchers("/createCustomer").permitAll()
                .antMatchers("/add-card").permitAll()
                .antMatchers("/customer").permitAll()
                .antMatchers("/addCustomer").permitAll()
                .antMatchers("/attach-payment-method").permitAll()
                .antMatchers("/showCard").permitAll()
                .antMatchers("/getAllPaymentMethods").permitAll()
                .antMatchers("/add-card.js").permitAll()
                .antMatchers("/checkout.js").permitAll()
                .antMatchers("/checkout.css").permitAll()
                .antMatchers("/customer/{customerId}/cards").permitAll()
                .antMatchers("/customerCards").permitAll()
                .antMatchers("/customer/{customerId}/chargeDetails").permitAll()
                .antMatchers("chargeDetails").permitAll()
                .antMatchers("/deleteCustomer/{customerId}").permitAll()
                .antMatchers("/detachCard/{customerId}/{cardLast4}").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() // Allow access to Swagger UI
                .anyRequest().authenticated();
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//class ends here
}

