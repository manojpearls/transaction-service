package com.natwest.transaction.service;

import com.natwest.transaction.client.AuthClient;
import com.natwest.transaction.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthClient authClient;

    public List<AccountDTO> getAccounts(){
        WellKnownEndPoint wellKnownEndPoint = authClient.getWellKnownEndPoint();
        AccessToken accessToken = authClient.getAccessToken("accounts",wellKnownEndPoint.getToken_endpoint());
        String consentId= authClient.getAccountConsent(accessToken.getAccess_token());
        String authorizationCode = authClient.approveConsent(wellKnownEndPoint.getAuthorization_endpoint(),consentId);
        authorizationCode = authClient.getExchangeAccessToken(wellKnownEndPoint.getToken_endpoint(), authorizationCode);

        List<Account> accounts = authClient.getAccountList(authorizationCode);

        List<AccountDTO> accountDTOList = new ArrayList<>();

        if (accounts != null) {
            for (Account account : accounts) {
                AccountDTO accountDTO = new AccountDTO(account.getNickname(), account.getAccountId(),account.getAccountType(),account.getAccountSubType(),account.getDescription(),authorizationCode);
                accountDTOList.add(accountDTO);
            }
        }
        return accountDTOList;
    }

}
