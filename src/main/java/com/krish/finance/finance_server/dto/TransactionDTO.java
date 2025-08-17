package com.krish.finance.finance_server.dto;

import com.krish.finance.finance_server.model.TransactionType;

import java.time.Instant;

public class TransactionDTO {
    private Double amount;
    private TransactionType type;
    private Long categoryId;
    private String description;
    private Instant date;

    public TransactionDTO() {}

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
}
