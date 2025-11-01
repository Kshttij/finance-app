package com.financeapp.backend.service;

import com.financeapp.backend.repo.UserRepository;
import com.financeapp.backend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

/**
 * Interview Point: Explain this class.
 *
 * "This is my UserService. Its job is to handle all direct
 * business logic related to the 'User' entity.
 * It's the only place (outside of Auth) that talks to the UserRepository.
 *
 * Crucially, when a user is saved, this service is responsible
 * for *always* encoding the password. The controller doesn't
 * know this is happening; it's an internal business rule."
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a user and hashes their password.
     * This is used by the registration process.
     */
    public User saveUser(User user) {
        // Hash the plain-text password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        return userRepository.save(user);
    }

    /**
     * Finds a user by their username.
     * Used by the UserDetailsService and our security helpers.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Checks if a username is already taken.
     * Used by the registration process.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    // --- Other potential methods ---

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}