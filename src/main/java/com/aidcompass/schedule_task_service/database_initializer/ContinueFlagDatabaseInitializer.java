package com.aidcompass.schedule_task_service.database_initializer;

import com.aidcompass.schedule_task_service.models.entity.ContinueFlagEntity;
import com.aidcompass.schedule_task_service.models.enums.TaskType;
import com.aidcompass.schedule_task_service.repositories.ContinueFlagRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class ContinueFlagDatabaseInitializer {

    private final ContinueFlagRepository repository;


    @PostConstruct
    public void setUpDatabase() {
        List<ContinueFlagEntity> entityList = Arrays.stream(TaskType.values())
                .map(type -> new ContinueFlagEntity(type, true, 50))
                .toList();
        repository.saveAll(entityList);
    }
}
