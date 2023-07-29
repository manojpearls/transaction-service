package com.natwest.transaction.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        // Create a custom HttpClient that ignores SSL certificates
        CloseableHttpClient httpClient = HttpClients.custom()
                //.disableRedirectHandling()
              //  .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        // Use the custom HttpClient in the HttpComponentsClientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Create and configure the RestTemplate using the custom request factory
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }
}
