package com.krish.finance.finance_server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.repository.CategoryRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            // INCOME categories
            categoryRepository.save(new Category("SALARY", TransactionType.INCOME));
            categoryRepository.save(new Category("FREELANCE", TransactionType.INCOME));

            // EXPENSE categories
            categoryRepository.save(new Category("RENT", TransactionType.EXPENSE));
            categoryRepository.save(new Category("GROCERIES", TransactionType.EXPENSE));
            categoryRepository.save(new Category("SUBSCRIPTIONS", TransactionType.EXPENSE));
            categoryRepository.save(new Category("ENTERTAINMENT", TransactionType.EXPENSE));
            categoryRepository.save(new Category("OTHERS", TransactionType.EXPENSE));
        }
    }
}

