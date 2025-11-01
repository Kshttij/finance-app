package com.financeapp.backend.service;

import com.financeapp.backend.dto.BudgetRequest;
import com.financeapp.backend.model.*;
import com.financeapp.backend.repo.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Interview Point: Explain this class.
 *
 * "This is the service layer for Budgets. It handles all the business
 * logic for creating, reading, and deleting budgets. It's the only
 * class that talks directly to the BudgetRepository."
 */
@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepo;

    public BudgetService(BudgetRepository budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    /**
     * Creates a new budget from a DTO and associates it with a user.
     * @param budgetRequest The DTO with new budget data
     * @param user The user who will own this budget
     * @return The saved Budget entity
     */
    public Budget create(BudgetRequest budgetRequest, User user) {
        // Convert the DTO into a database entity
        Budget newBudget = new Budget(
                budgetRequest.getName(),
                budgetRequest.getAmount(),
                user
        );
        // Save it to the database
        return budgetRepo.save(newBudget);
    }

    /**
     * Lists all budgets for a given user.
     */
    public List<Budget> listByUser(User user) {
        return budgetRepo.findAllByUserOrderByCreatedAt(user);
    }

    /**
     * Deletes a budget by its ID.
     */
    public void delete(String id) {
        budgetRepo.deleteById(id);
    }

    /**
     * Finds a single budget by its ID.
     * We return an Optional to let the Controller decide
     * what to do if the budget isn't found (e.g., return 404).
     */
    public Optional<Budget> findById(String id) {
        return budgetRepo.findById(id);
    }
}