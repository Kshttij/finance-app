package com.krish.finance.finance_server.controller;

import com.krish.finance.finance_server.dto.TransactionDTO;
import com.krish.finance.finance_server.model.Transaction;
import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ---------------- Create a transaction ----------------
    @PostMapping
    public Transaction createTransaction(
            @PathVariable Long userId,
            @RequestBody TransactionDTO transactionDTO) {

        Transaction tx = convertDtoToTransaction(transactionDTO);
        return transactionService.createTransaction(userId, tx);
    }

    // ---------------- Update a transaction ----------------
    @PutMapping("/{transactionId}")
    public Transaction updateTransaction(
            @PathVariable Long userId,
            @PathVariable Long transactionId,
            @RequestBody TransactionDTO transactionDTO) {

        Transaction updatedTx = convertDtoToTransaction(transactionDTO);
        return transactionService.updateTransaction(userId, transactionId, updatedTx);
    }

    // ---------------- Get all transactions ----------------
    @GetMapping
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return transactionService.getTransactionsForUser(userId);
    }

    // ---------------- Get transactions by type ----------------
    @GetMapping("/type/{type}")
    public List<Transaction> getTransactionsByType(
            @PathVariable Long userId,
            @PathVariable TransactionType type) {
        return transactionService.getTransactionsForUserByType(userId, type);
    }

    // ---------------- Get transactions by category ----------------
    @GetMapping("/category/{categoryId}")
    public List<Transaction> getTransactionsByCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        Category category = new Category();
        category.setId(categoryId);
        return transactionService.getTransactionsForUserByCategory(userId, category);
    }

    // ---------------- Get transactions in a date range ----------------
    @GetMapping("/range")
    public List<Transaction> getTransactionsInRange(
            @PathVariable Long userId,
            @RequestParam(required = false) Instant start,
            @RequestParam(required = false) Instant end) {
        return transactionService.getTransactionsForUserInRange(userId, start, end);
    }

    // ---------------- Delete a transaction ----------------
    @DeleteMapping("/{transactionId}")
    public boolean deleteTransaction(
            @PathVariable Long userId,
            @PathVariable Long transactionId) {
        return transactionService.deleteTransaction(userId, transactionId);
    }

    // ---------------- Get transactions in a month ----------------
    @GetMapping("/monthly")
    public List<Transaction> getTransactionsInMonth(
            @PathVariable Long userId,
            @RequestParam String startOfMonth,
            @RequestParam String endOfMonth) {

        Instant start = Instant.parse(startOfMonth);
        Instant end = Instant.parse(endOfMonth);

        return transactionService.getTransactionsForUserInMonth(userId, start, end);
    }

    // ---------------- Helper ----------------
    private Transaction convertDtoToTransaction(TransactionDTO dto) {
        Transaction tx = new Transaction();
        tx.setAmount(dto.getAmount());
        tx.setType(dto.getType());
        tx.setDescription(dto.getDescription());
        tx.setDate(dto.getDate() != null ? dto.getDate() : Instant.now());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            tx.setCategory(category);
        }

        return tx;
    }
}
