package com.krish.finance.finance_server.service;

import com.krish.finance.finance_server.model.User;
import com.krish.finance.finance_server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Create a new user
    public User createUser(User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email); // may return null
    }

    // Update user details
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null); // unwrap Optional
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
            
            return userRepository.save(existingUser);
        }
        return null;
    }

    // Delete a user
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null); // unwrap Optional
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
    
    public boolean checkPassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
}

}
