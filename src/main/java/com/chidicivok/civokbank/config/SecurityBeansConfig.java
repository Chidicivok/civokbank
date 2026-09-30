package com.chidicivok.civokbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * This is another configuration class that tells Spring that it can be used for Bean creation
 * */

@Configuration
public class SecurityBeansConfig {

    // Method requesting that a new instance of BCryptPasswordEncoder should be returned on bean creation to be used for password encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
