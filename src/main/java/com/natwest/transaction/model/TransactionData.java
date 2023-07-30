package com.natwest.transaction.model;

import lombok.Data;

@Data
public class TransactionData {
    private String transactionBookedTimestamp;
    private String transactionId;
    private String accountId;
    private double amount;
    private String transactionType;
    private String transactionCode;
    private String brand;
    private String transactionStatus;
    private String transactionNarrative;
    private String currencyCode;
    private Category category;
    private String carbonModelVersion;
    private double totalKgCo2e;
}
