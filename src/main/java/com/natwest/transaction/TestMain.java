package com.natwest.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natwest.transaction.model.AccountList;

public class TestMain {



    public static void main(String args[]){
        String data="{\"Data\":{\"Account\":[{\"AccountId\":\"03ea4134-2a8d-4899-b541-0a61594f17a1\",\"Currency\":\"GBP\",\"AccountType\":\"Personal\",\"AccountSubType\":\"CurrentAccount\",\"Description\":\"Personal\",\"Nickname\":\"my new accountss\",\"Account\":[{\"SchemeName\":\"UK.OBIE.SortCodeAccountNumber\",\"Identification\":\"82361586388766\",\"Name\":\"my new accountss\"}]},{\"AccountId\":\"1c9fe54d-b81a-4a71-822d-fbe8b0c9418f\",\"Currency\":\"GBP\",\"AccountType\":\"Personal\",\"AccountSubType\":\"CurrentAccount\",\"Description\":\"Personal\",\"Nickname\":\"Sydney Beard\",\"Account\":[{\"SchemeName\":\"UK.OBIE.SortCodeAccountNumber\",\"Identification\":\"50000012345601\",\"Name\":\"Sydney Beard\"}]},{\"AccountId\":\"6205da77-47a2-4c3f-b41c-4a87b6f28b6b\",\"Currency\":\"GBP\",\"AccountType\":\"Personal\",\"AccountSubType\":\"Savings\",\"Description\":\"Personal\",\"Nickname\":\"Sydney Beard\",\"Account\":[{\"SchemeName\":\"UK.OBIE.SortCodeAccountNumber\",\"Identification\":\"50000012345602\",\"Name\":\"Sydney Beard\"}]}]},\"Links\":{\"Self\":\"https://ob.sandbox.natwest.com/open-banking/v3.1/aisp/accounts\"},\"Meta\":{\"TotalPages\":1}}";
        AccountList accountList =null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            accountList = objectMapper.readValue(data, AccountList.class);
            // Now you can access the data in the accountList object

            System.out.println(accountList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
