package com.financeapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    /**
     * Interview Point: Why is this in its own file?
     *
     * "I created this @Bean for the BCryptPasswordEncoder in a separate AppConfig file.
     * This avoids a 'circular dependency' problem.
     *
     * My UserService needs to hash passwords, so it needs the PasswordEncoder.
     * My SecurityConfig needs to check passwords, so it also needs the PasswordEncoder.
     * But, SecurityConfig ALSO needs my UserService.
     *
     * If I put the @Bean in SecurityConfig, UserService would depend on SecurityConfig,
     * and SecurityConfig would depend on UserService. This is a circle.
     *
     * By putting the bean here, both services can depend on AppConfig,
     * and the circle is broken."
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}