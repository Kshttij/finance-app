package com.krish.finance.finance_server.repository;

import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Fetch categories by type (INCOME or EXPENSE)
    List<Category> findByType(TransactionType type);
}