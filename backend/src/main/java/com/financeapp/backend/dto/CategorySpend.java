package com.financeapp.backend.dto;

import com.financeapp.backend.model.Category; // <-- Import the Category enum

// A simple DTO to hold the results of our GROUP BY query
public class CategorySpend {
    private Category category;
    private Double total;

    // --- THIS IS THE FIX ---
    // The constructor now correctly accepts the 'Category' enum,
    // which matches what the JPQL query is sending.
    public CategorySpend(Category category, Double total) {
        this.category = category;
        this.total = total;
    }

    // Add getters
    // The JSON converter (Jackson) will automatically turn the
    // enum into a string (e.g., "FOOD") in the final JSON.
    public Category getCategory() { return category; }
    public Double getTotal() { return total; }
}
