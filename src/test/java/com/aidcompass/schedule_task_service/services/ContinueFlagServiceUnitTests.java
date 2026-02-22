package com.aidcompass.schedule_task_service.services;

import com.aidcompass.schedule_task_service.exceptions.models.ContinueFlagNotFoundByTypeException;
import com.aidcompass.schedule_task_service.models.dto.ContinueFlagDto;
import com.aidcompass.schedule_task_service.models.entity.ContinueFlagEntity;
import com.aidcompass.schedule_task_service.models.enums.TaskType;
import com.aidcompass.schedule_task_service.repositories.ContinueFlagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContinueFlagServiceUnitTests {

    @Mock
    private ContinueFlagRepository repository;

    @InjectMocks
    private ContinueFlagService service;

    @Test
    @DisplayName("UT: update() when flag exists should update entity and return dto")
    void update_whenFlagExists_shouldUpdateEntityAndReturnDto() {
        // given
        ContinueFlagDto dto = new ContinueFlagDto(TaskType.DELETE_INTERVAL_BATCH, 50, false);
        ContinueFlagEntity entity = new ContinueFlagEntity();
        entity.setBatchSize(20);
        entity.setShouldContinue(true);
        when(repository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.of(entity));

        // when
        ContinueFlagDto result = service.update(dto);

        // then
        assertEquals(dto, result);
        ArgumentCaptor<ContinueFlagEntity> captor = ArgumentCaptor.forClass(ContinueFlagEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(50, captor.getValue().getBatchSize());
        assertFalse(captor.getValue().isShouldContinue());
    }

    @Test
    @DisplayName("UT: update() when flag not found should throw exception")
    void update_whenFlagNotFound_shouldThrowException() {
        // given
        ContinueFlagDto dto = new ContinueFlagDto(TaskType.DELETE_INTERVAL_BATCH, 50, false);
        when(repository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContinueFlagNotFoundByTypeException.class, () -> service.update(dto));
        verify(repository, never()).save(any());
    }
}
