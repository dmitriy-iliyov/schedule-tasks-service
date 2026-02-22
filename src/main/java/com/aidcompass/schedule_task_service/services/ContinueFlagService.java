package com.aidcompass.schedule_task_service.services;

import com.aidcompass.schedule_task_service.exceptions.models.ContinueFlagNotFoundByTypeException;
import com.aidcompass.schedule_task_service.models.dto.ContinueFlagDto;
import com.aidcompass.schedule_task_service.models.entity.ContinueFlagEntity;
import com.aidcompass.schedule_task_service.repositories.ContinueFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContinueFlagService {

    private final ContinueFlagRepository repository;


    @Transactional
    public ContinueFlagDto update(ContinueFlagDto dto) {
        ContinueFlagEntity entity = repository.findByType(dto.taskType()).orElseThrow(
                ContinueFlagNotFoundByTypeException::new
        );
        entity.setBatchSize(dto.batchSize());
        entity.setShouldContinue(dto.shouldContinue());
        repository.save(entity);
        return dto;
    }
}
