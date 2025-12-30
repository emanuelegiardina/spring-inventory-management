package com.order.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient productWebClient(
            OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${services.product.base-url}") String productBaseUrl
    ) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        oauth2.setDefaultClientRegistrationId("product-client"); 
        // client confidential (client_credentials) -->product-client

        return WebClient.builder()
                .baseUrl(productBaseUrl)   // path dell'hostname, in produzione basta modificare solo application.yml!! 
                .apply(oauth2.oauth2Configuration())
                .build();
    }
/*
    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("product-client"); // client confidential configurato in application.yml

        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }*/
}