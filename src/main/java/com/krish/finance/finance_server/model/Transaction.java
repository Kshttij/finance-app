package com.krish.finance.finance_server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

@ManyToOne
@JoinColumn(name = "category_id")
private Category category;

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(nullable = false)
    private Instant date = Instant.now();

    public Transaction() {}

    public Transaction(User user, TransactionType type, Category category, Double amount, String description) {
        this.user = user;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = Instant.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
}
