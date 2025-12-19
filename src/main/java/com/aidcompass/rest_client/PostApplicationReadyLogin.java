package com.aidcompass.rest_client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostApplicationReadyLogin {

    private final LoginRestClient restClient;

    @EventListener(ApplicationReadyEvent.class)
    public void loginAfterApplicationStart() {
        restClient.login();
    }
}
