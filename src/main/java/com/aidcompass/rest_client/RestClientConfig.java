package com.aidcompass.rest_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${aidcompass.api.protocol}")
    private String protocol;

    @Value("${aidcompass.api.host}")
    private String host;

    @Value("${aidcompass.api.port}")
    private String port;


    @Bean
    public ScheduleSystemRestClient scheduleSystemRestClient() {
        String baseUrl = protocol + "://" + host + ":" + port + "/api/system/v1";
        return new ScheduleSystemRestClient(RestClient.builder().baseUrl(baseUrl).build());
    }
}
