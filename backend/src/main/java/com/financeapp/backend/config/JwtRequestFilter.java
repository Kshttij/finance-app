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

/**
 * Interview Point: Explain this class.
 *
 * "This class, JwtRequestFilter, is the heart of my JWT security.
 * It's a filter that runs ONCE for EVERY request that comes into the server.
 * Its job is to check if a request has a valid JWT and, if it does,
 * to log the user in for that single request."
 */
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

        // --- Step 1: Get the Authorization header ---
        // This is where the token is sent from the frontend.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // --- Step 2: Check if the header is valid and extract the token ---
        // A valid header looks like: "Bearer [long_token_string]"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token string (everything after "Bearer ")
            jwt = authorizationHeader.substring(7);
            
            try {
                // Use the JwtUtil to parse the token and get the username
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (ExpiredJwtException e) {
                // Token is expired, we'll let it fail later
                // The user is not authenticated
            } catch (Exception e) {
                // Other issues (malformed token, etc.)
            }
        }

        // --- Step 3: Validate the token and set the user in the Security Context ---
        
        // Check 1: We successfully extracted a username from the token.
        // Check 2: The SecurityContextHolder has no authentication. This means
        //          the user is NOT already logged in for this request.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user's details from the database (via our service)
            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);

            // Check 3: Use JwtUtil to validate the token.
            // This checks the signature and expiration date, AND
            // ensures the username in the token matches the userDetails.
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // --- If all checks pass, we manually authenticate the user ---
                
                // Create the authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Set details about the request (e.g., IP address)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ** THIS IS THE KEY LINE **
                // We set the user in Spring Security's context.
                // Spring now considers this user "authenticated" for this request.
                // Our controllers can now access this user via the 'Principal' object.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // --- Step 4: Pass the request to the next filter in the chain ---
        // This is crucial. We must call this so the request can continue
        // to the next filter and eventually to our controller.
        chain.doFilter(request, response);
    }
}