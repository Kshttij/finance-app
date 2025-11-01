package com.financeapp.backend.service;

import com.financeapp.backend.model.*;
import com.financeapp.backend.repo.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepo;
    public BudgetService(BudgetRepository budgetRepo){ this.budgetRepo = budgetRepo; }

    public Budget create(Budget b){ return budgetRepo.save(b); }
    public List<Budget> listByUser(User user){ return budgetRepo.findAllByUserOrderByCreatedAt(user); }
    public void delete(String id){ budgetRepo.deleteById(id); }
    public Budget findById(String id){ return budgetRepo.findById(id).orElse(null); }
}
