package com.financeapp.backend.config;

import com.financeapp.backend.model.User;
import com.financeapp.backend.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // For authorities/roles
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Find User entity from our database
        Optional<User> myUserOptional = userService.getUserByUsername(username);

        // 2. if/else check for clarity
        if (myUserOptional.isPresent()) {
            User myUser = myUserOptional.get();

            // 3. Convert our User object into a Spring Security UserDetails object
            // We are not using roles, so we pass an empty list for authorities.
            return new org.springframework.security.core.userdetails.User(
                    myUser.getUsername(),
                    myUser.getPassword(),
                    new ArrayList<>() // Empty list for authorities
            );
        } else {
            // 4. If the user isn't found, we must throw this specific exception
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}