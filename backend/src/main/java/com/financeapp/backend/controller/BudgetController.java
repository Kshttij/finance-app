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
 * These are all SECURED endpoints, so Spring Security only allows
 * access if the user provides a valid JWT.
 * We get the authenticated user by adding the 'Principal' object
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
        //  Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);

        //ensure user is present
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            //service to find budgets
            return ResponseEntity.ok(budgetService.listByUser(user));
        } else {
            // If no user is found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
    }

    
      //Gets a single budget by its ID.
     
    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable String id, Principal principal) {
        //  Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        //  Find the budget from the database
        // We now get an Optional back from the service
        Optional<Budget> budgetOptional = budgetService.findById(id);

        //check to see if the budget exists
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            
            // Verify the user owns this budget
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

    
     //Creates a new budget for the logged-in user.
     
    @PostMapping
    public ResponseEntity<?> createBudget(@Valid @RequestBody BudgetRequest budgetRequest, Principal principal) {
        // Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        //  Delegate creation to the service
        // The service will handle creating the Budget object from the DTO
        Budget newBudget = budgetService.create(budgetRequest, loggedInUser);
        
        // Return the newly created budget
        return ResponseEntity.status(HttpStatus.CREATED).body(newBudget);
    }

    
     // Deletes a budget by its ID.
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable String id, Principal principal) {
        //  Get the logged-in user
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        User loggedInUser = userOptional.get();

        //  Find the budget
        Optional<Budget> budgetOptional = budgetService.findById(id);

        //  check if it exists
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();

            //  Verify ownership
            if (budget.getUser().getId().equals(loggedInUser.getId())) {
                // If checks pass, delete it
                budgetService.delete(id);
                // Return 204 No Content (standard for a successful delete)
                return ResponseEntity.noContent().build();
            } else {
                // If they don't own it, return 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this budget");
            }
        } else {
            // If it doesn't exist, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        }
    }
}