package com.aidcompass.task_type;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskTypeCacheInitializer {

    private final TaskTypeService service;

    @EventListener(ApplicationReadyEvent.class)
    public void setUpCache() {
        service.setUpCache();
    }
}
