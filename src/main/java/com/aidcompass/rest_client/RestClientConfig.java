package com.aidcompass.rest_client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${aidcompass.api.protocol}")
    private String protocol;

    @Value("${aidcompass.api.host}")
    private String host;

    @Value("${aidcompass.api.port}")
    private String port;


    @Bean
    public LoginRestClient loginRestClient() {
        String baseUrl = protocol + "://" + host + ":" + port + "/api/system/v1/auth/login";
        return new LoginRestClient(RestClient
                .builder()
                .baseUrl(baseUrl)
                .build()
        );
    }

    @Bean
    public BearerTokenInterceptor bearerTokenInterceptor(LoginRestClient loginRestClient) {
        return new BearerTokenInterceptor(loginRestClient);
    }

    @Bean
    public BearerTokenErrorHandler bearerTokenErrorHandler(LoginRestClient loginRestClient) {
        return new BearerTokenErrorHandler(loginRestClient);
    }

    @Bean
    public ScheduleSystemRestClient scheduleSystemRestClient(BearerTokenInterceptor bearerTokenInterceptor,
                                                             BearerTokenErrorHandler bearerTokenErrorHandler) {
        String baseUrl = protocol + "://" + host + ":" + port + "/api/system/v1";
        return new ScheduleSystemRestClient(RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestInterceptor(bearerTokenInterceptor)
                .defaultStatusHandler(bearerTokenErrorHandler)
                .build()
        );
    }
}
