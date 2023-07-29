package com.natwest.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natwest.transaction.client.AuthClient;
import com.natwest.transaction.model.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    AuthClient authClient;

    public List<TransactionDTO> getAllTransaction(String id, HttpServletRequest httpServletRequest) {
        String transaction = authClient.getTransaction(id, httpServletRequest.getHeader("authorizationCode"));

        List<TransactionDTO> transactions = new ArrayList<>();
        Iterator<JsonNode> elements;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(transaction);
            JsonNode jsonNodeData = jsonNode.get("Data");
            if (jsonNodeData.size() == 0)
                return transactions;
            elements = jsonNodeData.get("Transaction").elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                TransactionDTO transactionDTO = TransactionDTO.builder()
                        //.customerId("12345")
                        .transactionId(element.get("TransactionId").asText(""))
                        .transactionCode(element.get("ProprietaryBankTransactionCode").get("Code").asText(""))
                        .brand("NWB")
                        .amount(element.get("Amount").get("Amount").asDouble(0))
                        .description(element.get("TransactionInformation").asText(""))
                        .transactionType(element.get("CreditDebitIndicator").asText(""))
                        .isEcoProduct(true)
                        //.alternateProduct(someListOfProducts)
                        .carbonEmission(new BigDecimal("5.25"))
                        .currencyCode(element.get("Amount").get("Currency").asText(""))
                        .category("Electronics")
                        .datetime(Instant.parse(element.get("BookingDateTime").asText()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .build();
                transactions.add(transactionDTO);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return transactions;
    }


    public boolean addTransaction(TransactionDTO transactionDTO) {
        return true;
    }
}
