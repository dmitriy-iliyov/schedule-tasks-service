package com.aidcompass.rest_client;

import com.aidcompass.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSystemRestClient {

    private final RestClient restClient;
    private final static int MAX_ATTEMPT = 1;


    public List<Long> deleteIntervalsBatchBeforeWeakStart(int batchSize) {
        return deleteIntervalsBatchBeforeWeakStart(batchSize, 0);
    }

    private List<Long> deleteIntervalsBatchBeforeWeakStart(int batchSize, int attempt) {
        try {
            return restClient
                    .delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/intervals/past/batch")
                            .queryParam("batch_size", batchSize)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (RestClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && attempt < MAX_ATTEMPT) {
                return deleteIntervalsBatchBeforeWeakStart(batchSize, ++attempt);
            }
            throw new ApiException(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while calling cleanup endpoint", e);
            throw new ApiException("Unexpected error while deleting intervals!");
        }
    }

    public List<Long> markAppointmentBatchAsSkipped(int batchSize) {
        return markAppointmentBatchAsSkipped(batchSize, 0);
    }

    private List<Long> markAppointmentBatchAsSkipped(int batchSize, int attempt) {
        try {
            return restClient
                    .patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/appointments/past/batch/skip")
                            .queryParam("batch_size", batchSize)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (RestClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && attempt < MAX_ATTEMPT) {
                return markAppointmentBatchAsSkipped(batchSize, ++attempt);
            }
            throw new ApiException(e.getMessage());
        }
    }

    public Boolean notifyBatch(int batchSize, int page) {
        Map<String, String> requestBody =
                Map.of(
                        "batch_size", String.valueOf(batchSize),
                        "page", String.valueOf(page)
                );
        return notifyBatch(requestBody, 0);
    }

    private Boolean notifyBatch(Map<String, String> requestBody, int attempt) {
        try {
            return restClient
                    .post()
                    .uri("/appointments/batch/remind")
                    .body(requestBody)
                    .retrieve()
                    .body(Boolean.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && attempt < MAX_ATTEMPT) {
                return notifyBatch(requestBody, ++attempt);
            }
            throw new ApiException(e.getMessage());
        }
    }
}