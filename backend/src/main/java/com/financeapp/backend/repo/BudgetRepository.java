package com.financeapp.backend.repo;

import com.financeapp.backend.model.Budget;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, String> {
    List<Budget> findAllByUserOrderByCreatedAt(User user);
}
