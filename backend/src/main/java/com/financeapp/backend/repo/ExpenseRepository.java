package com.financeapp.backend.repo;

import com.financeapp.backend.model.Expense;
import com.financeapp.backend.model.User;
import com.financeapp.backend.model.Budget; // Need this for the findAllByBudget
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    // "Find all Expenses for a User, ordered by newest first"
    List<Expense> findAllByUserOrderByCreatedAtDesc(User user);
    
    // "Find all Expenses for a specific Budget object"
    List<Expense> findAllByBudget(Budget budget);

    // "Find all Expenses for a User AND a specific budget ID"
    // Spring Data JPA understands this method name:
    // "findBy" (the prefix)
    // "User" (the 'user' field in the Expense model)
    // "And"
    // "Budget_Id" (the 'id' field *inside* the 'budget' field)
    List<Expense> findByUserAndBudget_Id(User user, String budgetId);
}