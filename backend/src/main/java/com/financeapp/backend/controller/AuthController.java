package com.financeapp.backend.controller;

import com.financeapp.backend.dto.AuthRequest;
import com.financeapp.backend.dto.AuthResponse;
import com.financeapp.backend.dto.UserRequest;
import com.financeapp.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

/**
 * Interview Point: Explain this class.
 *
 * "This is my AuthController. It's the API layer for handling
 * public endpoints: '/register' and '/login'.
 *
 * I've refactored this to follow clean code principles:
 * 1. It only accepts DTOs (Data Transfer Objects), not database models.
 * 2. It uses the '@Valid' annotation to automatically trigger validation.
 * 3. It's a 'thin controller' - all the real work (hashing,
 * token generation) is delegated to a dedicated 'AuthService'."
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // We only inject the new AuthService, which handles all the logic
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            // Delegate the entire registration process to the auth service
            authService.register(userRequest);
            return ResponseEntity.ok("User registered successfully");
        
        } catch (RuntimeException e) {
            // Catch exceptions from the service (e.g., "Username already exists")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            // Delegate the entire login process to the auth service
            AuthResponse authResponse = authService.login(authRequest);
            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException e) {
            // If the service throws this, it means username/password was wrong
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}