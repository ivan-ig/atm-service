package com.github.ivanig.atmserver.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${webClient.bankServerURL}")
    private String baseUrl;

    @Bean
    public WebClient webClientBean() {
        return WebClient.create(baseUrl);
    }
}
