package com.natwest.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    String accountName;
    String accountId;
    String accountType;
    String accountSubType;
    String description;
    String authorizationCode;
}
