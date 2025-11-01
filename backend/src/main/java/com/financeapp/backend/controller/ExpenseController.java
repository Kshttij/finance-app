package com.financeapp.backend.controller;

import com.financeapp.backend.dto.ExpenseRequest;
import com.financeapp.backend.model.Expense;
import com.financeapp.backend.model.User;
import com.financeapp.backend.service.ExpenseService;
import com.financeapp.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Interview Point: Explain this class.
 *
 * "This controller handles all '/api/expenses' endpoints. It's very
 * similar to the BudgetController. It's fully secured and uses
 * DTOs for creating new expenses. It also has a helper
 * for getting the logged-in user."
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    private Optional<User> getLoggedInUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userService.getUserByUsername(principal.getName());
    }

    /**
     * Lists expenses. This endpoint is smart:
     * 1. If no 'budgetId' is given, it returns ALL expenses for the user.
     * 2. If a 'budgetId' is given, it returns only expenses for that budget.
     */
    @GetMapping
    public ResponseEntity<?> listExpenses(
            Principal principal,
            @RequestParam(required = false) String budgetId
    ) {
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOptional.get();

        List<Expense> expenses;
        
        // This is the logic to handle the optional query parameter
        if (budgetId != null && !budgetId.isEmpty()) {
            // Case 1: Filter by a specific budget
            expenses = expenseService.listByUserAndBudget(user, budgetId);
        } else {
            // Case 2: Get all expenses
            expenses = expenseService.listByUser(user);
        }
        
        return ResponseEntity.ok(expenses);
    }

    /**
     * Creates a new expense.
     */
    @PostMapping
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest, Principal principal) {
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        try {
            // Delegate the entire creation and validation logic to the service
            Expense newExpense = expenseService.createExpense(expenseRequest, loggedInUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
        
        } catch (RuntimeException e) {
            // Catch errors from the service, like "Budget not found"
            // or "You do not own this budget"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Deletes an expense by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id, Principal principal) {
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        // 1. Find the expense
        Optional<Expense> expenseOptional = expenseService.findById(id);

        // 2. Verbose check if it exists
        if (expenseOptional.isPresent()) {
            Expense expense = expenseOptional.get();
            
            // 3. CRITICAL SECURITY CHECK: Verify ownership
            if (expense.getUser().getId().equals(loggedInUser.getId())) {
                // 4. If checks pass, delete it
                expenseService.delete(id);
                return ResponseEntity.noContent().build();
            } else {
                // 5. If not the owner, return 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this expense");
            }
        } else {
            // 6. If it doesn't exist, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }
    }
}