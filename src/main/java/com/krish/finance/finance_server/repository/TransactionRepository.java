package com.krish.finance.finance_server.repository;

import com.krish.finance.finance_server.model.Transaction;
import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    List<Transaction> findByUserIdAndCategory(Long userId, Category category);

    List<Transaction> findByUserIdAndDateBetween(Long userId, Instant start, Instant end);
}
