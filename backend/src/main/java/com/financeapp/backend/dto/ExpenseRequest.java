package com.financeapp.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// A DTO for creating a new expense.
public class ExpenseRequest {

    @NotBlank(message = "Expense name cannot be blank")
    private String name;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be zero or more")
    private Double amount;

    @NotBlank(message = "Budget ID cannot be blank")
    private String budgetId;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}