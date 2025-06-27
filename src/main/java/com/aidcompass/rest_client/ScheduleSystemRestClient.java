package com.aidcompass.rest_client;

import com.aidcompass.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSystemRestClient {

    @Value("${service.name}")
    private String serviceName;

    @Value("${service.key}")
    private String serviceKey;

    private final RestClient restClient;
    private final static String BEARER_TEMPLATE = "Bearer %s";


    @EventListener(ApplicationReadyEvent.class)
    public void loginAfterApplicationStart() {
        login();
    }

    public String login() {
        Map<String, String> credentials = Map.of(
                "service_name", serviceName,
                "service_key", serviceKey
        );
        try {
            return restClient
                    .post()
                    .uri("/auth/login")
                    .body(credentials)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    public List<Long> deleteIntervalsBatchBeforeWeakStart(int batchSize) {
        try {
            return restClient
                    .delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/intervals/past/batch")
                            .queryParam("batch_size", batchSize)
                            .build())
//                    .header("Authorization", BEARER_TEMPLATE.formatted(jwt))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    public List<Long> markAppointmentBatchAsSkipped(int batchSize) {
        try {
            return restClient
                    .patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/appointments/past/batch/skip")
                            .queryParam("batch_size", batchSize)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }
}
