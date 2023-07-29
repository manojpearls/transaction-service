package com.natwest.transaction.model;

import com.natwest.transaction.model.Data;
import com.natwest.transaction.model.Links;
import com.natwest.transaction.model.Meta;
import com.natwest.transaction.model.Risk;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountConsentRequest {
    private Data data;
    private Risk risk;
    private Links links;
    private Meta meta;
}

