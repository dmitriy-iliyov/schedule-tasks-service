package com.aidcompass.rest_client;

import com.aidcompass.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSystemRestClient {

    private final RestClient restClient;


    public List<Long> deleteIntervalsBatchBeforeWeakStart(int batchSize) {
        try {
            return restClient
                    .delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/intervals/past/batch")
                            .queryParam("batch_size", batchSize)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ApiException();
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
            throw new ApiException();
        }
    }
}
