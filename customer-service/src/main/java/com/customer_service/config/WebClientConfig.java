package com.customer_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${webclient.order-service.base-url}")
    private String orderServiceBaseUrl;

    @Value("${webclient.payment-service.base-url}")
    private String paymentServiceBaseUrl;

    @Value("${webclient.order-service.connect-timeout}")
    private int orderServiceConnectTimeout;

    @Value("${webclient.order-service.read-timeout}")
    private int orderServiceReadTimeout;

    @Value("${webclient.payment-service.connect-timeout}")
    private int paymentServiceConnectTimeout;

    @Value("${webclient.payment-service.read-timeout}")
    private int paymentServiceReadTimeout;

    @Bean
    public WebClient orderServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(orderServiceBaseUrl).build();
    }

    @Bean
    public WebClient paymentServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(paymentServiceBaseUrl).build();
    }

}
