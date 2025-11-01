package com.financeapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Interview Point: Explain this class.
 *
 * "This is the main Spring Security configuration. It defines all my
 * security rules for the application. It's where I bring all the
 * other pieces (like the JwtRequestFilter) together."
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * This bean creates the 'AuthenticationManager', which is what
     * Spring Security uses to process a login attempt.
     * We configure it to use our custom 'UserDetailsService' (to find users)
     * and 'BCrypt' (to check passwords).
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder
                .userDetailsService(myUserDetailsService) // How to find the user
                .passwordEncoder(bCryptPasswordEncoder);  // How to check the password
        
        return authenticationManagerBuilder.build();
    }

    /**
     * This bean defines our 'SecurityFilterChain', which is the
     * set of rules that HttpSecurity will apply to all requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // --- 1. CORS Configuration ---
            // "First, I enable CORS using the 'corsConfigurationSource' bean below.
            // This is necessary to allow my frontend (on localhost:5173)
            // to make requests to my backend (on localhost:8080)."
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // --- 2. CSRF Disablement ---
            // "Next, I disable CSRF (Cross-Site Request Forgery) protection.
            // This is standard for a stateless REST API that uses JWTs,
            // as JWTs are inherently protected against CSRF."
            .csrf(csrf -> csrf.disable())

            // --- 3. Endpoint Permissions ---
            // "This is where I define which endpoints are public and which are private."
            .authorizeHttpRequests(authz -> authz
                    // "I permit all requests to '/api/auth/register' and '/api/auth/login'."
                    .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                    // "For any other request, the user must be authenticated."
                    .anyRequest().authenticated()
            )

            // --- 4. Session Management ---
            // "This is a critical line for a JWT-based app.
            // I set the session creation policy to 'STATELESS'.
            // This tells Spring Security NOT to create or use sessions.
            // Each request must be authenticated independently with its JWT."
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // --- 5. Add our Custom Filter ---
            // "Finally, I add my custom 'JwtRequestFilter'.
            // I tell Spring to run it BEFORE its default login filter.
            // This ensures my token is checked on every request."
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * This bean configures CORS (Cross-Origin Resource Sharing).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 1. Set which origins (frontends) are allowed
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        
        // 2. Set which HTTP methods are allowed
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 3. Set which headers are allowed
        // This is CRITICAL: we must allow 'Authorization' so the frontend
        // can send us the JWT.
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to all paths under "/api/"
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}