package com.financeapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // ✅ Import this
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonIgnore // ✅ Tell Jackson to ignore this field
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // ✅ Tell Jackson to ignore this field
    private User user;

    public Expense() {
    }

    public Expense(String name, Double amount, Budget budget, User user) {
        this.name = name;
        this.amount = amount;
        this.budget = budget;
        this.user = user;
        this.createdAt = Instant.now();
    }

    // ✅ ADD THESE METHODS
    // Jackson will see these and serialize them as "budgetId" and "userId"
    
    public String getBudgetId() {
        return (budget != null) ? budget.getId() : null;
    }

    public Long getUserId() {
        return (user != null) ? user.getId() : null;
    }


    // --- getters/setters (no changes needed below) ---

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
}