package com.financeapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private Double amount;
    private Instant createdAt;

    /**
     * Interview Point: Explain this '@JsonIgnore'
     *
     * "This is a Many-to-One relationship, meaning many budgets can
     * belong to one user.
     *
     * I've marked the 'User' object with '@JsonIgnore' and 'FetchType.LAZY'.
     * This is critical for two reasons:
     * 1. LAZY: It tells Hibernate not to load the User object from the
     * database unless I explicitly ask for it (e.g., budget.getUser()).
     * 2. JsonIgnore: It tells the JSON serializer (Jackson) to *never*
     * include the full User object when sending a Budget to the frontend.
     * This prevents accidentally leaking sensitive user data
     * and also stops infinite loops (if User had a List<Budget>)."
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Budget() {
    }

    public Budget(String name, Double amount, User user) {
        this.name = name;
        this.amount = amount;
        this.user = user;
        this.createdAt = Instant.now();
    }

    /**
     * Interview Point: Explain this custom getter.
     *
     * "Since I ignored the 'User' object, my frontend still needs
     * to know which user this budget belongs to.
     *
     * I created this custom getter 'getUserId()'. Jackson sees this
     * and adds a new field called 'userId' to the final JSON,
     * which only contains the ID. This is a secure and clean
     * way to expose the foreign key."
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}