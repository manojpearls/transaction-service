package com.natwest.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail {
    private String SchemeName;
    private String Identification;
    private String Name;
}

