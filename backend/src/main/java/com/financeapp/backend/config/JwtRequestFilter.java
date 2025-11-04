package com.financeapp.backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            
            try {
                // --- VALIDATION STEP 1: Check Signature & Expiration ---
                // This line will fail if the token is expired (ExpiredJwtException)
                // or if the signature is fake (SignatureException, etc.)
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (ExpiredJwtException e) {
                // Token is expired, log it if you want, but don't authenticate
            } catch (Exception e) {
                // Token is fake/malformed, don't authenticate
            }
        }

        // --- VALIDATION STEP 2: Check User Existence & Set Security Context ---
        
        // We only proceed if:
        // 1. We successfully got a username from a valid token (Step 1)
        // 2. The user is not *already* authenticated for this session
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // --- VALIDATION STEP 3: Check if user exists in our DB ---
            // This loads the user from our database.
            // If the user was deleted, this will throw a UsernameNotFoundException,
            // the code will stop, and the user won't be authenticated.
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            // --- ALL CHECKS PASSED! ---
            // If we are here, we know:
            // 1. The token is authentic (signature is good).
            // 2. The token is not expired.
            // 3. The user in the token *actually exists* in our database.

            // We can now manually authenticate the user for this single request.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            
            // Set the user in Spring Security's context.
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        
        // Pass the request to the next filter and eventually to the controller
        chain.doFilter(request, response);
    }
}