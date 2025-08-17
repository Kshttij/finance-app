package com.krish.finance.finance_server.dto;

import com.krish.finance.finance_server.model.TransactionType;

public class TransactionRequest {
    private Double amount;
    private String description;
    private TransactionType type;
    private Long categoryId;

    public TransactionRequest() {}

    public TransactionRequest(Double amount, String description, TransactionType type, Long categoryId) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.categoryId = categoryId;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}

