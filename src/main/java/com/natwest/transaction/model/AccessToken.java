package com.natwest.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;
}

