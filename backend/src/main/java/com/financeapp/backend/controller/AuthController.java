package com.financeapp.backend.controller;

import com.financeapp.backend.config.JwtUtil;
import com.financeapp.backend.model.User;
import com.financeapp.backend.service.UserService;

// [REMOVED] import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        // [FIX] This is the new JWT login flow
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            // 1. Try to authenticate the user using Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            // 2. If authentication fails, return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // 3. If authentication is successful, load UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 4. Generate a JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // 5. Return the token in the response
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "username", userDetails.getUsername(),
                "message", "Login successful"
        ));
    }
}