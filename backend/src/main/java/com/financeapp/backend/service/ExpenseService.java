package com.financeapp.backend.service;

import com.financeapp.backend.dto.ExpenseRequest;
import com.financeapp.backend.model.*;
import com.financeapp.backend.repo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Interview Point: Explain this class.
 *
 * "This is the service layer for Expenses. It contains all the
 * business logic. A key feature here is in the 'createExpense'
 * method, where it performs validation *before* creating an expense."
 */
@Service
public class ExpenseService {
    
    private final ExpenseRepository expenseRepo;
    private final BudgetService budgetService; // Need this to validate the budget

    public ExpenseService(ExpenseRepository expenseRepo, BudgetService budgetService) {
        this.expenseRepo = expenseRepo;
        this.budgetService = budgetService;
    }

    /**
     * Creates a new expense, but first validates the budget.
     * @param expenseRequest The DTO with new expense data
     * @param user The user who will own this expense
     * @return The saved Expense entity
     */
    public Expense createExpense(ExpenseRequest expenseRequest, User user) {
        
        // --- Validation Logic ---
        // 1. Find the budget the user wants to add this expense to
        Optional<Budget> budgetOptional = budgetService.findById(expenseRequest.getBudgetId());

        if (budgetOptional.isEmpty()) {
            // The budget ID they provided doesn't exist
            throw new RuntimeException("Budget not found with ID: " + expenseRequest.getBudgetId());
        }
        
        Budget budget = budgetOptional.get();

        // 2. Security Check: Make sure the budget belongs to the logged-in user
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not own this budget");
        }
        // --- End Validation ---

        // 3. If all checks pass, create the new Expense entity
        Expense newExpense = new Expense(
                expenseRequest.getName(),
                expenseRequest.getAmount(),
                budget,
                user
        );
        
        // 4. Save and return the new expense
        return expenseRepo.save(newExpense);
    }

    /**
     * Lists all expenses for a given user.
     */
    public List<Expense> listByUser(User user) {
        return expenseRepo.findAllByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Lists all expenses for a user *and* a specific budget ID.
     */
    public List<Expense> listByUserAndBudget(User user, String budgetId) {
        return expenseRepo.findByUserAndBudget_Id(user, budgetId);
    }

    /**
     * Finds a single expense by its ID.
     */
    public Optional<Expense> findById(String id) {
        return expenseRepo.findById(id);
    }

    /**
     * Deletes an expense by its ID.
     */
    public void delete(String id) {
        expenseRepo.deleteById(id);
    }
}