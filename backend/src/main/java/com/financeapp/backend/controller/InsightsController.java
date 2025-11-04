package com.financeapp.backend.controller;

import com.financeapp.backend.dto.CategorySpend;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repo.ExpenseRepository;
import com.financeapp.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/insights")
public class InsightsController {

    private final UserService userService;
    private final ExpenseRepository expenseRepo;

    public InsightsController(UserService userService, ExpenseRepository expenseRepo) {
        this.userService = userService;
        this.expenseRepo = expenseRepo;
    }

    // Helper to get the user
    private Optional<User> getLoggedInUser(Principal principal) {
        if (principal == null) return Optional.empty();
        return userService.getUserByUsername(principal.getName());
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSpendingSummary(Principal principal) {
        Optional<User> userOptional = getLoggedInUser(principal);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOptional.get();

        // Call our new repository method!
        List<CategorySpend> summary = expenseRepo.findSpendingByCategory(user);
        
        // This returns JSON like: [{"category": "FOOD", "total": 400.00}, ...]
        // This is the "insight" for your dashboard.
        return ResponseEntity.ok(summary);
    }
}