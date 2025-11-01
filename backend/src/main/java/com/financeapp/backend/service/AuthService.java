package com.financeapp.backend.service;

import com.financeapp.backend.config.JwtUtil;
import com.financeapp.backend.dto.AuthRequest;
import com.financeapp.backend.dto.AuthResponse;
import com.financeapp.backend.dto.UserRequest;
import com.financeapp.backend.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Interview Point: Explain this class.
 *
 * "I created a new AuthService to clean up my AuthController.
 * This service now holds all the 'business logic' for
 * registering and logging in.
 *
 * The controller's job is just to handle the HTTP request and DTOs.
 * This service's job is to orchestrate all the *other* services
 * (like UserService, AuthenticationManager, JwtUtil)
 * to perform the full authentication flow."
 */
@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles the user registration logic.
     * @param userRequest The DTO containing registration info
     */
    public void register(UserRequest userRequest) {
        // 1. Check if the username is already taken
        if (userService.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username already registered");
        }
        
        // 2. Create a new User entity from the DTO
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(userRequest.getPassword()); // Pass the plain text

        // 3. Save the user. The UserService will handle hashing the password.
        userService.saveUser(newUser);
    }

    /**
     * Handles the user login logic.
     * @param authRequest The DTO containing login credentials
     * @return An AuthResponse with the token and username
     * @throws BadCredentialsException if authentication fails
     */
    public AuthResponse login(AuthRequest authRequest) throws BadCredentialsException {
        // 1. Try to authenticate the user
        // This will use our MyUserDetailsService and BCrypt encoder.
        // If it fails, it throws a BadCredentialsException.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        // 2. If authentication is successful, load UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
                authRequest.getUsername()
        );

        // 3. Generate a JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4. Return the response DTO
        return new AuthResponse(
                jwt,
                userDetails.getUsername(),
                "Login successful"
        );
    }
}