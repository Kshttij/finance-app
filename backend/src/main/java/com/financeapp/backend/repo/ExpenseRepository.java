package com.financeapp.backend.repo;

// --- ADD THESE IMPORTS ---
import com.financeapp.backend.dto.CategorySpend;
import com.financeapp.backend.model.Expense;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// --- END IMPORTS ---

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {

    // Standard "find by" query. Spring Data JPA builds this for you.
    List<Expense> findAllByUserOrderByCreatedAtDesc(User user);
    
    // Another standard "find by" query.
    List<Expense> findByUserAndBudget_Id(User user, String budgetId);
    
    // --- THIS IS THE "INSIGHTS" QUERY ---
    // It now knows what @Query, CategorySpend, and @Param are.
    @Query("SELECT new com.financeapp.backend.dto.CategorySpend(e.category, SUM(e.amount)) " +
           "FROM Expense e WHERE e.user = :user GROUP BY e.category")
    List<CategorySpend> findSpendingByCategory(@Param("user") User user);

    // @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.budget.id = :budgetId")
    // Double sumExpensesByBudgetId(@Param("budgetId") String budgetId);
}