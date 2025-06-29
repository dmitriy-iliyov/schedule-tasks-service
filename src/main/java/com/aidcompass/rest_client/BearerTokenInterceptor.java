package com.aidcompass.rest_client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class BearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private final LoginRestClient restClient;


    @EventListener(ApplicationReadyEvent.class)
    public void loginAfterApplicationStart() {
        restClient.login();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + restClient.getToken());
        return execution.execute(request, body);
    }
}