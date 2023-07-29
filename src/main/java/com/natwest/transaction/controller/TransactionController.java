package com.natwest.transaction.controller;

import com.natwest.transaction.model.TransactionDTO;
import com.natwest.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @GetMapping("/transactions/{accountId}")
    public List<TransactionDTO> getAllTransactions(@PathVariable String accountId) {
        return transactionService.getAllTransaction(accountId,httpServletRequest);
    }


    @PostMapping("/transactions")
    public boolean createTransaction(@RequestBody TransactionDTO newTransaction) {
       return transactionService.addTransaction(newTransaction);
    }
}
