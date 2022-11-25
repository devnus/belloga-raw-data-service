package com.devnus.belloga.data.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${app.web-client.user-service-base-url}")
    private String USER_SERVICE_BASE_URL;
    @Value("${app.web-client.labeling-service-base-url}")
    private String LABELING_SERVICE_BASE_URL;

    @Bean(name = "userServiceWebClient")
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl(USER_SERVICE_BASE_URL)
                .build();
    }

    @Bean(name = "labelingServiceWebClient")
    public WebClient labelingServiceWebClient() {
        return WebClient.builder()
                .baseUrl(LABELING_SERVICE_BASE_URL)
                .build();
    }

}
