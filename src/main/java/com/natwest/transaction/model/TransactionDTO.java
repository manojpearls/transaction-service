package com.natwest.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private String transactionId;
    private String transactionCode;
    private String brand;
    private double amount;
    private String description;
    private String transactionType;
    private boolean isEcoProduct;
    private List<Product> alternateProduct;
    private BigDecimal carbonEmission;
    private String currencyCode;
    private String category;
    private LocalDateTime datetime;

    public enum TransactionType {
        DEBIT, CREDIT
    }
}

