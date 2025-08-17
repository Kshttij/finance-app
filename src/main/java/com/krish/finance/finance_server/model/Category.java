package com.krish.finance.finance_server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., Salary, Rent, Groceries

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // INCOME or EXPENSE

    public Category() {}

    public Category(String name, TransactionType type) {
        this.name = name;
        this.type = type;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
