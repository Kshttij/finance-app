package com.financeapp.backend.service;

import com.financeapp.backend.model.*;
import com.financeapp.backend.repo.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // [FIX] Import Optional

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepo;

    public ExpenseService(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    public Expense create(Expense e) {
        return expenseRepo.save(e);
    }

    public List<Expense> listByUser(User user) {
        return expenseRepo.findAllByUserOrderByCreatedAtDesc(user);
    }

    public List<Expense> listByBudget(Budget budget) {
        return expenseRepo.findAllByBudget(budget);
    }

    // [FIX] We need this method to verify ownership before deleting
    public Optional<Expense> findById(String id) {
        return expenseRepo.findById(id);
    }

    public void delete(String id) {
        expenseRepo.deleteById(id);
    }

    // ✅ ADD THIS NEW METHOD
    public List<Expense> listByUserAndBudget(User user, String budgetId) {
        return expenseRepo.findByUserAndBudget_Id(user, budgetId);
    }
}