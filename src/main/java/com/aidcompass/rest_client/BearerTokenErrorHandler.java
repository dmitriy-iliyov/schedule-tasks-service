package com.aidcompass.rest_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@Slf4j
public class BearerTokenErrorHandler implements ResponseErrorHandler {

    private final LoginRestClient restClient;


    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            return true;
        }
        return false;
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if (response.getHeaders().containsKey("WWW-Authenticate")) {
            restClient.login();
            return;
        }
        log.error("url={}, method={}, response={}", url, method, response);
    }
}
