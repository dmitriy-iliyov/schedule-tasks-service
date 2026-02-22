package com.aidcompass.schedule_task_service.rest_client;

import com.aidcompass.schedule_task_service.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSystemRestClient {

    private final RestClient restClient;
    private static final int MAX_RETRIES = 1;

    public List<Long> deleteIntervalsBatchBeforeWeakStart(int batchSize) {
        return executeWithRetry(() -> restClient
                        .delete()
                        .uri(uriBuilder -> uriBuilder
                                .path("/intervals/past/batch")
                                .queryParam("batch_size", batchSize)
                                .build())
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {}),
                "Unexpected error while deleting intervals!");
    }

    public List<Long> markAppointmentBatchAsSkipped(int batchSize) {
        return executeWithRetry(() -> restClient
                        .patch()
                        .uri(uriBuilder -> uriBuilder
                                .path("/appointments/past/batch/skip")
                                .queryParam("batch_size", batchSize)
                                .build())
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {}),
                "Unexpected error while marking appointments as skipped!");
    }

    public Boolean notifyBatch(int batchSize, int page) {
        Map<String, String> requestBody =
                Map.of(
                        "batch_size", String.valueOf(batchSize),
                        "page", String.valueOf(page)
                );
        return executeWithRetry(() -> restClient
                .post()
                .uri("/appointments/batch/remind")
                .body(requestBody)
                .retrieve()
                .body(Boolean.class), "Unexpected error while notifying for appointments!");
    }

    private <T> T executeWithRetry(Supplier<T> restOperation, String errorMessage) {
        return executeWithRetry(restOperation, errorMessage, 0);
    }

    private <T> T executeWithRetry(Supplier<T> restOperation, String errorMessage, int attempt) {
        try {
            return restOperation.get();
        } catch (RestClientResponseException e) {
            log.error("Request failed with status {} and message: {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && attempt < MAX_RETRIES) {
                log.info("Retrying operation, attempt {} of {}", attempt + 1, MAX_RETRIES);
                return executeWithRetry(restOperation, errorMessage, attempt + 1);
            }
            throw new ApiException(e.getMessage());
        } catch (Exception e) {
            log.error(errorMessage, e);
            throw new ApiException(errorMessage);
        }
    }
}