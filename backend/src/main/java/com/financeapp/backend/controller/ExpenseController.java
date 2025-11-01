package com.financeapp.backend.controller;

import com.financeapp.backend.model.*;
import com.financeapp.backend.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, BudgetService budgetService, UserService userService) {
        this.expenseService = expenseService;
        this.budgetService = budgetService;
        this.userService = userService;
    }

    private Optional<User> getLoggedInUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userService.getUserByUsername(principal.getName());
    }

    // ✅ MODIFIED THIS METHOD
    @GetMapping
    public ResponseEntity<?> list(
            Principal principal,
            @RequestParam(required = false) String budgetId // <-- Reads the optional budgetId
    ) {
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        // ✅ ADDED THIS LOGIC
        if (budgetId != null && !budgetId.isEmpty()) {
            // If budgetId is provided, filter by user AND budget
            // (This requires the new method in ExpenseService)
            return ResponseEntity.ok(expenseService.listByUserAndBudget(user, budgetId));
        } else {
            // If no budgetId, return all expenses for the user
            return ResponseEntity.ok(expenseService.listByUser(user));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, Principal principal) {
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedInUser = userOpt.get();

        String name = (String) body.get("name");
        Double amount = Double.valueOf(body.get("amount").toString());
        String budgetId = (String) body.get("budgetId");
        
        Budget budget = budgetService.findById(budgetId);

        if (budget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }

        if (!budget.getUser().getId().equals(loggedInUser.getId())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
        }

        Expense e = new Expense(name, amount, budget, loggedInUser);
        return ResponseEntity.ok(expenseService.create(e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOpt.get();

        Optional<Expense> expenseOpt = expenseService.findById(id);

        if (expenseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }

        Expense expense = expenseOpt.get();

        if (!expense.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this expense");
        }

        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}