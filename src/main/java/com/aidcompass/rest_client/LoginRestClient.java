package com.aidcompass.rest_client;

import com.aidcompass.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class LoginRestClient {

    @Value("${service.name}")
    private String serviceName;

    @Value("${service.key}")
    private String serviceKey;

    private final RestClient restClient;
    private String jwt;


    public void login() {
        Map<String, String> credentials = Map.of(
                "service_name", serviceName,
                "service_key", serviceKey
        );
        try {
            jwt = Objects.requireNonNull(restClient
                    .post()
                    .body(credentials)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, String>>() {
                    })).get("token");
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    public String getToken() {
        return jwt;
    }
}
