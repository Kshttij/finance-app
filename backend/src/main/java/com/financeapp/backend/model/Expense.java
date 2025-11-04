package com.financeapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private Double amount;
    private Instant createdAt;

    @Enumerated(EnumType.STRING) 
    private Category category;

    /**
     * This is the relationship to the Budget.
     * Just like on the Budget, we use @JsonIgnore and FetchType.LAZY
     * to prevent leaks and infinite loops.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonIgnore
    private Budget budget;

    /**
     * This is the relationship to the User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Expense() {
    }

    public Expense(String name, Double amount, Category category,Budget budget, User user) {
        this.name = name;
        this.amount = amount;
        this.budget = budget;
        this.user = user;
        this.createdAt = Instant.now();
        this.category = category;
    }

    /**
     * Custom getter to expose the 'budgetId' in the JSON.
     * My frontend needs this to know which budget the expense belongs to.
     */
    public String getBudgetId() {
        return (budget != null) ? budget.getId() : null;
    }

    /**
     * Custom getter to expose the 'userId' in the JSON.
     */
    public Long getUserId() {
        return (user != null) ? user.getId() : null;
    }

    // --- Standard Getters and Setters ---
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Budget getBudget() {
        return budget;
    }
    public void setBudget(Budget budget) {
        this.budget = budget;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}