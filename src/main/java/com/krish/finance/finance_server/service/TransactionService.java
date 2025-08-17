package com.krish.finance.finance_server.service;

import com.krish.finance.finance_server.model.Transaction;
import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.User;
import com.krish.finance.finance_server.repository.TransactionRepository;
import com.krish.finance.finance_server.repository.UserRepository;
import com.krish.finance.finance_server.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // -------------------- CRUD Methods --------------------

    public Transaction createTransaction(Long userId, Transaction tx) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        tx.setUser(user);

        if (tx.getDate() == null) tx.setDate(Instant.now());

        if (tx.getCategory() == null || !categoryRepository.existsById(tx.getCategory().getId())) {
            tx.setCategory(categoryRepository.findByType(tx.getType()).stream().findFirst().orElse(null));
        }

        return transactionRepository.save(tx);
    }

    public Transaction updateTransaction(Long userId, Long transactionId, Transaction updated) {
        Transaction existing = transactionRepository.findById(transactionId).orElse(null);
        if (existing == null || existing.getUser() == null || !existing.getUser().getId().equals(userId)) return null;

        if (updated.getType() != null) existing.setType(updated.getType());
        if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
        if (updated.getAmount() != null) existing.setAmount(updated.getAmount());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getDate() != null) existing.setDate(updated.getDate());

        return transactionRepository.save(existing);
    }

    public boolean deleteTransaction(Long userId, Long transactionId) {
        Transaction existing = transactionRepository.findById(transactionId).orElse(null);
        if (existing == null || existing.getUser() == null || !existing.getUser().getId().equals(userId)) return false;

        transactionRepository.delete(existing);
        return true;
    }

    // -------------------- Query Methods --------------------

    public List<Transaction> getTransactionsForUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsForUserByType(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    public List<Transaction> getTransactionsForUserByCategory(Long userId, Category category) {
        return transactionRepository.findByUserIdAndCategory(userId, category);
    }

    public List<Transaction> getTransactionsForUserInRange(Long userId, Instant start, Instant end) {
        if (start != null && end != null) {
            return transactionRepository.findByUserIdAndDateBetween(userId, start, end);
        }
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsForUserInMonth(Long userId, Instant startOfMonth, Instant endOfMonth) {
    return transactionRepository.findByUserIdAndDateBetween(userId, startOfMonth, endOfMonth);
}

}
