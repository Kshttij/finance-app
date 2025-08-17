package com.krish.finance.finance_server.controller;

import com.krish.finance.finance_server.model.Category;
import com.krish.finance.finance_server.model.TransactionType;
import com.krish.finance.finance_server.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Fetch all categories
    @GetMapping("/api/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Fetch categories by type (for dropdown filtering)
    @GetMapping("/api/categories/by-type")
    public List<Category> getCategoriesByType(@RequestParam TransactionType type) {
        return categoryRepository.findByType(type);
    }
}
