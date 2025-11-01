package com.financeapp.backend.repo;

import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // "Find a User by their username"
    Optional<User> findByUsername(String username);

    // "Check if a User exists with this username"
    boolean existsByUsername(String username);
}