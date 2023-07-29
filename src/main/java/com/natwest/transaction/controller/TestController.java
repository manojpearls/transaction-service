package com.natwest.transaction.controller;

import com.natwest.transaction.client.AuthClient;
import com.natwest.transaction.model.WellKnownEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    AuthClient authClient;

    @GetMapping("/wellknown")
    public WellKnownEndPoint testWellKnown() {
        //return transactionService.getAllTransaction();
        return authClient.getWellKnownEndPoint();
    }
}
