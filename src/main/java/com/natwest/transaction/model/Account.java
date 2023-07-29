package com.natwest.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Account {
    private String AccountId;
    private String Currency;
    private String AccountType;
    private String AccountSubType;
    private String Description;
    private String Nickname;
    private List<AccountDetail> Account;
}

