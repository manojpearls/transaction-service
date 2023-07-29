package com.natwest.transaction.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natwest.transaction.model.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class AuthClient {

    @Value("${wellKnownEndpoint}")
    String wellKnownEndpoint;

    @Value("${clientId}")
    String clientId;

    @Value("${clientSecret}")
    String clientSecret;

    @Value("${apiUrlPrefix}")
    String apiUrlPrefix;

    @Value("${keycloakUrl}")
    private String keycloakUrl;

    @Value("${realmName}")
    private String realmName;

    @Value("${tokenDomain}")
    private String tokenDomain;

    @Value("${domainName}")
    private String domainName;

    @Value("${redirectUrl}")
    private String redirectUrl;

    @Value("${financialId}")
    private String financialId;

    @Value("${psuUsername}")
    private String psuUsername;

    @Value("${psuPassword}")
    private String psuPassword;

    @Value("${psuDebtorAccountIdentification}")
    private String psuDebtorAccountIdentification;

    @Value("${psuDebtorAccountScheme}")
    private String psuDebtorAccountScheme;

    @Value("${psuDebtorAccountCurrency}")
    private String psuDebtorAccountCurrency;

    @Value("${psuDebtorAccountName}")
    private String psuDebtorAccountName;

    @Value("${psuDebtorAccountType}")
    private String psuDebtorAccountType;

    @Autowired
    RestTemplate restTemplate;

    @Cacheable("wellKnownEndPointCache")
    public WellKnownEndPoint getWellKnownEndPoint(){
        WellKnownEndPoint wellKnownEndPoint = restTemplate.getForObject(wellKnownEndpoint, WellKnownEndPoint.class);
        return wellKnownEndPoint;
    }

    public AccessToken getAccessToken(String scope, String url){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("scope", scope);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request and map the response to AccessTokenResponse class
        ResponseEntity<AccessToken> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AccessToken.class
        );
        return response.getBody();
    }

    public String getAccountConsent(String authorizationToken){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+authorizationToken);

        String requestBodyJson = "{\"Data\":{\"Permissions\":[\"ReadAccountsDetail\",\"ReadBalances\",\"ReadTransactionsCredits\",\"ReadTransactionsDebits\",\"ReadTransactionsDetail\"]},\"Risk\":{}}";

        // Create the HttpEntity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrlPrefix+"/open-banking/v3.1/aisp/account-access-consents",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return getConsentIdFromJsonString(responseEntity.getBody());
    }

    public String approveConsent(String authorizationEndPointUrl,String consentId){
        StringBuilder builder = new StringBuilder(authorizationEndPointUrl);
        builder.append("?client_id=").append(clientId)
                .append("&response_type=code id_token")
                .append("&scope=openid accounts")
                .append("&redirect_uri=").append(redirectUrl)
                .append("&state=ABC")
                .append("&request=").append(consentId)
                .append("&authorization_mode=AUTO_POSTMAN")
                .append("&authorization_username=").append(psuUsername);

        String finalUrl = builder.toString();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toString(),
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        String responseBody = responseEntity.getBody();
        String authorizationCode = extractAuthorizationCode(responseBody);

        return authorizationCode;
    }

    public String getExchangeAccessToken(String tokenEndPoint,String authorizationCode){
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("client_id", clientId);
        formParameters.add("client_secret", clientSecret);
        formParameters.add("redirect_uri", redirectUrl);
        formParameters.add("grant_type", "authorization_code");
        formParameters.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParameters, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                tokenEndPoint,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String responseBody = responseEntity.getBody();
        String accessToken = parseAccessToken(responseBody);

        return accessToken;

    }

    public  List<Account> getAccountList(String authorizationCode){
        String url = apiUrlPrefix + "/open-banking/v3.1/aisp/accounts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+authorizationCode);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        List<Account> accounts = new ArrayList<>();
        Iterator<JsonNode> elements;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode jsonNodeData = jsonNode.get("Data");
            elements = jsonNodeData.get("Account").elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                Account account = new Account();
                account.setAccountId(element.get("AccountId").asText());
                account.setCurrency(element.get("Currency").asText());
                account.setAccountType(element.get("AccountType").asText());
                account.setAccountSubType(element.get("AccountSubType").asText());
                account.setDescription(element.get("Description").asText());
                account.setNickname(element.get("Nickname").asText());

                // Account array
                JsonNode accountArray = element.get("Account");
                if (accountArray != null && accountArray.isArray() && accountArray.size() > 0) {
                    List<AccountDetail> accountDetails = new ArrayList<>();
                    for (JsonNode accountObject : accountArray) {
                        AccountDetail accountDetail = new AccountDetail();
                        accountDetail.setSchemeName(accountObject.get("SchemeName").asText());
                        accountDetail.setIdentification(accountObject.get("Identification").asText());
                        accountDetail.setName(accountObject.get("Name").asText());
                        accountDetails.add(accountDetail);
                    }
                    account.setAccount(accountDetails);
                }

                accounts.add(account);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public String getTransaction(String accountId,String authorizationCode){
        String url = apiUrlPrefix + "/open-banking/v3.1/aisp/accounts/"+accountId+"/transactions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+authorizationCode);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    private String extractAuthorizationCode(String responseBody) {
        String redirectUri = responseBody;

        String[] splittedUri = redirectUri.split("#");
        String queryParams = splittedUri[splittedUri.length - 1];
        String[] splittedQueryParams = queryParams.split("&");

        String authorizationCode = null;
        for (String queryParam : splittedQueryParams) {
            String[] keyValue = queryParam.split("=");
            if (keyValue[0].equals("code")) {
                authorizationCode = keyValue[1];
                break;
            }
        }
        return authorizationCode;
    }

    private String parseAccessToken(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConsentIdFromJsonString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Extract the "ConsentId" value
          //  JsonNode firstElement = jsonNode.get(0);
            JsonNode dataNode = jsonNode.get("Data");
            String consentId = dataNode.get("ConsentId").asText();

            return consentId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private  RestTemplate createRestTemplateWithRedirects() {
        HttpClient httpClient = HttpClients.custom()
                .disableRedirectHandling() // Enable redirects
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
