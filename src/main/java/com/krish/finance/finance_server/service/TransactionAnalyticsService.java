package com.krish.finance.finance_server.service;

import com.krish.finance.finance_server.model.Transaction;
import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionAnalyticsService {

    private final TransactionRepository transactionRepository;

    public TransactionAnalyticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Total income or expense for a user
    public double getTotalForUserByType(Long userId, TransactionType type) {
        List<Transaction> list = transactionRepository.findByUserIdAndType(userId, type);
        return list.stream()
                   .map(Transaction::getAmount)
                   .filter(amount -> amount != null)
                   .mapToDouble(Double::doubleValue)
                   .sum();
    }

    // Expense totals grouped by category
    public Map<String, Double> getExpenseTotalsByCategory(Long userId, Instant start, Instant end) {
        List<Transaction> transactions;
        if (start != null && end != null) {
            transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        Map<String, Double> totals = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                String catName = t.getCategory() != null ? t.getCategory().getName() : "Uncategorized";
                totals.put(catName, totals.getOrDefault(catName, 0.0) + (t.getAmount() != null ? t.getAmount() : 0.0));
            }
        }
        return totals;
    }

    // Monthly expense summary
    public Map<String, Double> getMonthlyExpenseSummary(Long userId, Instant start, Instant end) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);
        Map<String, Double> summary = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                String catName = t.getCategory() != null ? t.getCategory().getName() : "Uncategorized";
                summary.put(catName, summary.getOrDefault(catName, 0.0) + (t.getAmount() != null ? t.getAmount() : 0.0));
            }
        }
        return summary;
    }
}
