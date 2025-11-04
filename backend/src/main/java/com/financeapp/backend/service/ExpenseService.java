package com.financeapp.backend.service;

import com.financeapp.backend.dto.ExpenseRequest;
import com.financeapp.backend.model.*;
import com.financeapp.backend.model.Category; 
import com.financeapp.backend.repo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    
    private final ExpenseRepository expenseRepo;
    private final BudgetService budgetService; 

    public ExpenseService(ExpenseRepository expenseRepo, BudgetService budgetService) {
        this.expenseRepo = expenseRepo;
        this.budgetService = budgetService;
    }

    
    public Expense createExpense(ExpenseRequest expenseRequest, User user) {
        
        
        // 1. Find the budget the user wants to add this expense to
        Optional<Budget> budgetOptional = budgetService.findById(expenseRequest.getBudgetId());

        if (budgetOptional.isEmpty()) {
            // The budget ID they provided doesn't exist
            throw new RuntimeException("Budget not found with ID: " + expenseRequest.getBudgetId());
        }
        
        Budget budget = budgetOptional.get();

        //Make sure the budget belongs to the logged-in user
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not own this budget");
        }
       
        // ---CATEGORY LOGIC ---
        Category expenseCategory;
        try {
            // Convert the string from the DTO into our Category enum
            expenseCategory = Category.valueOf(expenseRequest.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            // If they send a bad string default it to OTHER
            expenseCategory = Category.OTHER;
        }
      

        //create the new Expense entity
        
        Expense newExpense = new Expense(expenseRequest.getName(),expenseRequest.getAmount(),expenseCategory,budget,user);
        return expenseRepo.save(newExpense);
    }

    
     //Lists all expenses for a given user.
    
    public List<Expense> listByUser(User user) {
        return expenseRepo.findAllByUserOrderByCreatedAtDesc(user);
    }

    
     // Lists all expenses for a user and a specific budget ID.
    
    public List<Expense> listByUserAndBudget(User user, String budgetId) {
        return expenseRepo.findByUserAndBudget_Id(user, budgetId);
    }

    public Optional<Expense> findById(String id) {
        return expenseRepo.findById(id);
    }

    public void delete(String id) {
        expenseRepo.deleteById(id);
    }
}