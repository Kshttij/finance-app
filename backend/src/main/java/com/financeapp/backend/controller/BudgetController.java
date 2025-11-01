package com.financeapp.backend.controller;

import com.financeapp.backend.model.*;
import com.financeapp.backend.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // [FIX] Standardize on java.security.Principal
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    // [FIX] Helper method to get the User object from the Principal
    private Optional<User> getLoggedInUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userService.getUserByUsername(principal.getName());
    }

    // list budgets for the logged-in user
    @GetMapping
    public ResponseEntity<?> list(Principal principal) {
        // [FIX] Use the Principal to get the user.
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        
        return ResponseEntity.ok(budgetService.listByUser(userOpt.get()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, Principal principal) {
        // [FIX] Get user from Principal, NOT from the request body.
        // This prevents one user from creating a budget for another user.
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        String name = (String) body.get("name");
        Double amount = Double.valueOf(body.get("amount").toString());

        Budget b = new Budget(name, amount, userOpt.get());
        return ResponseEntity.ok(budgetService.create(b));
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable String id, Principal principal) {
        // 1. Get the logged-in user
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOpt.get();

        // 2. Find the budget
        Budget budget = budgetService.findById(id);

        // 3. Check if it exists
        if (budget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }

        // 4. ✅ THE CRITICAL SECURITY CHECK
        // This is the same logic from your delete() method.
        if (!budget.getUser().getId().equals(loggedInUser.getId())) {
            // Return 403 Forbidden if the user does not own this budget
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
        }

        // 5. If all checks pass, return the budget
        return ResponseEntity.ok(budget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
        // [FIX] CRITICAL SECURITY FIX
        // We must verify that the budget being deleted belongs to the logged-in user.
        Optional<User> userOpt = getLoggedInUser(principal);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User loggedInUser = userOpt.get();
        Budget budget = budgetService.findById(id);

        if (budget == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }

        // Check if the budget's user ID matches the logged-in user's ID
        if (!budget.getUser().getId().equals(loggedInUser.getId())) {
            // [FIX] 403 Forbidden - You are logged in, but you don't have permission
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
        }

        // If all checks pass, then delete
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}