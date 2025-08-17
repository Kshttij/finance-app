package com.krish.finance.finance_server.controller;

import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.service.TransactionAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/users/{userId}/analytics")
public class TransactionAnalyticsController {

    private final TransactionAnalyticsService analyticsService;

    public TransactionAnalyticsController(TransactionAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // ---------------- Total income or expense ----------------
    @GetMapping("/total/{type}")
    public double getTotalByType(
            @PathVariable Long userId,
            @PathVariable TransactionType type) {
        return analyticsService.getTotalForUserByType(userId, type);
    }

    // ---------------- Expense totals grouped by category ----------------
    @GetMapping("/expenses-by-category")
    public Map<String, Double> getExpensesByCategory(
            @PathVariable Long userId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {

        Instant startInstant = start != null ? Instant.parse(start) : null;
        Instant endInstant = end != null ? Instant.parse(end) : null;

        return analyticsService.getExpenseTotalsByCategory(userId, startInstant, endInstant);
    }

    // ---------------- Monthly expense summary ----------------
    @GetMapping("/monthly-summary")
    public Map<String, Double> getMonthlySummary(
            @PathVariable Long userId,
            @RequestParam String startOfMonth,
            @RequestParam String endOfMonth) {

        Instant start = Instant.parse(startOfMonth);
        Instant end = Instant.parse(endOfMonth);

        return analyticsService.getMonthlyExpenseSummary(userId, start, end);
    }
}
