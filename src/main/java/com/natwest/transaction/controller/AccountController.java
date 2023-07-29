package com.natwest.transaction.controller;

import com.natwest.transaction.model.AccountDTO;
import com.natwest.transaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAccounts();
    }

}
