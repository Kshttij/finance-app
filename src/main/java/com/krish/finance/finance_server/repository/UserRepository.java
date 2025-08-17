package com.krish.finance.finance_server.repository;

import com.krish.finance.finance_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // returns null if not found
}
