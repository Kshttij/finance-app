package com.financeapp.backend.repo;

import com.financeapp.backend.model.Expense;
import com.financeapp.backend.model.User;
import com.financeapp.backend.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findAllByUserOrderByCreatedAtDesc(User user);
    List<Expense> findAllByBudget(Budget budget);

    // "Find by the 'user' object AND the 'id' property of the 'budget' object"
    List<Expense> findByUserAndBudget_Id(User user, String budgetId);
}
