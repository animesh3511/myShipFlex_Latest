package com.example.user_verification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UserVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserVerificationApplication.class, args);
	}

}
