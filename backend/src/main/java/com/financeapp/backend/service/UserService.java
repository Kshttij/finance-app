package com.financeapp.backend.service;

import com.financeapp.backend.repo.UserRepository;
import com.financeapp.backend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create or save a new user (registration)
    public User saveUser(User user) {
        // [REFACTOR] Password encoding logic is centralized here.
        // The controller shouldn't know or care about how passwords are handled.
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    // Get a user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get a user by email
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Update user details
    public User updateUser(User user) {
        // Note: If you update a password, you should re-encode it here too.
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Check if email already exists (for registration)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}