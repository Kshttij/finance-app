package com.financeapp.backend.controller;

import com.financeapp.backend.dto.BudgetRequest;
import com.financeapp.backend.model.Budget;
import com.financeapp.backend.model.User;
import com.financeapp.backend.service.BudgetService;
import com.financeapp.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Interview Point: Explain this class.
 *
 * "This is the controller for all '/api/budgets' endpoints.
 * These are all SECURED endpoints, so Spring Security only allows
 * access if the user provides a valid JWT.
 *
 * We get the authenticated user by adding the 'Principal' object
 * as a method argument. Spring populates this for us thanks
 * to our JwtRequestFilter."
 */
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    /**
     * Helper method to get our custom User object from the Principal.
     * The Principal only gives us the username, so we use our UserService
     * to fetch the full User object from the database.
     */
    private Optional<User> getLoggedInUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        String username = principal.getName();
        return userService.getUserByUsername(username);
    }

    /**
     * Gets a list of all budgets for the currently logged-in user.
     */
    @GetMapping
    public ResponseEntity<?> listBudgets(Principal principal) {
        // 1. Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);

        // 2. Verbose check to ensure user is present
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 3. Delegate to the service to find budgets
            return ResponseEntity.ok(budgetService.listByUser(user));
        } else {
            // 4. If no user is found (which shouldn't happen if security is on)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
    }

    /**
     * Gets a single budget by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable String id, Principal principal) {
        // 1. Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        // 2. Find the budget from the database
        // We now get an Optional back from the service
        Optional<Budget> budgetOptional = budgetService.findById(id);

        // 3. Verbose check to see if the budget exists
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            
            // 4. CRITICAL SECURITY CHECK: Verify the user owns this budget
            if (budget.getUser().getId().equals(loggedInUser.getId())) {
                // If they own it, return the budget
                return ResponseEntity.ok(budget);
            } else {
                // If they don't own it, return 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
            }
        } else {
            // 5. If the budget ID doesn't exist, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }
    }

    /**
     * Creates a new budget for the logged-in user.
     */
    @PostMapping
    public ResponseEntity<?> createBudget(@Valid @RequestBody BudgetRequest budgetRequest, Principal principal) {
        // 1. Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        // 2. Delegate creation to the service
        // The service will handle creating the Budget object from the DTO
        Budget newBudget = budgetService.create(budgetRequest, loggedInUser);
        
        // 3. Return the newly created budget
        return ResponseEntity.status(HttpStatus.CREATED).body(newBudget);
    }

    /**
     * Deletes a budget by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable String id, Principal principal) {
        // 1. Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        // 2. Find the budget
        Optional<Budget> budgetOptional = budgetService.findById(id);

        // 3. Verbose check if it exists
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();

            // 4. CRITICAL SECURITY CHECK: Verify ownership
            if (budget.getUser().getId().equals(loggedInUser.getId())) {
                // 5. If checks pass, delete it
                budgetService.delete(id);
                // Return 204 No Content (standard for a successful delete)
                return ResponseEntity.noContent().build();
            } else {
                // 6. If they don't own it, return 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
            }
        } else {
            // 7. If it doesn't exist, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }
    }
}