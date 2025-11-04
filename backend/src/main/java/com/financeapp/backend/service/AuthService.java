package com.financeapp.backend.service;

import com.financeapp.backend.config.JwtUtil;
import com.financeapp.backend.dto.AuthRequest;
import com.financeapp.backend.dto.AuthResponse;
import com.financeapp.backend.dto.UserRequest;
import com.financeapp.backend.model.User;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
   
    // Updated Constructor:
    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    
      //Handles the user registration logic.
     
    public void register(UserRequest userRequest) {
        if (userService.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username already registered");
        }
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(userRequest.getPassword()); 
        userService.saveUser(newUser);
    }

    public AuthResponse login(AuthRequest authRequest) throws BadCredentialsException {

        // find the user in the database
        User user = userService.getUserByUsername(authRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // check if the password matches
        boolean isPasswordMatch = passwordEncoder.matches(authRequest.getPassword(), // Plain-text password from request
                user.getPassword()         // Hashed password from database
        );

        if (!isPasswordMatch) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // If we are here, the password is correct!

        // Generate a JWT token
        final String jwt = jwtUtil.generateToken(user.getUsername());

        // 5. Return the response DTO
        return new AuthResponse(
                jwt,
                user.getUsername(),
                "Login successful"
        );
    }
}