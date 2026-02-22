package com.aidcompass.schedule_task_service.services;

import com.aidcompass.schedule_task_service.exceptions.models.ContinueFlagNotFoundByTypeException;
import com.aidcompass.schedule_task_service.models.entity.ContinueFlagEntity;
import com.aidcompass.schedule_task_service.models.entity.TaskEntity;
import com.aidcompass.schedule_task_service.models.enums.TaskStatus;
import com.aidcompass.schedule_task_service.models.enums.TaskType;
import com.aidcompass.schedule_task_service.repositories.ContinueFlagRepository;
import com.aidcompass.schedule_task_service.repositories.TaskRepository;
import com.aidcompass.schedule_task_service.rest_client.ScheduleSystemRestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleCleanUpServiceUnitTests {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ContinueFlagRepository continueFlagRepository;
    @Mock
    private ScheduleSystemRestClient restClient;

    @InjectMocks
    private ScheduleCleanUpService service;

    @Test
    @DisplayName("UT: deletePastIntervalBatch() when flag not found should throw exception")
    void deletePastIntervalBatch_whenFlagNotFound_shouldThrowException() {
        // given
        when(continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContinueFlagNotFoundByTypeException.class, () -> service.deletePastIntervalBatch());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: deletePastIntervalBatch() when shouldContinue is false should skip")
    void deletePastIntervalBatch_whenShouldContinueIsFalse_shouldSkip() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(false);
        when(continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.of(flag));

        // when
        service.deletePastIntervalBatch();

        // then
        verify(restClient, never()).deleteIntervalsBatchBeforeWeakStart(anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: deletePastIntervalBatch() when client returns items should complete task")
    void deletePastIntervalBatch_whenClientReturnsItems_shouldCompleteTask() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(20);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.deleteIntervalsBatchBeforeWeakStart(20)).thenReturn(List.of(1L, 2L));

        // when
        service.deletePastIntervalBatch();

        // then
        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(2)).save(taskCaptor.capture());
        List<TaskEntity> savedTasks = taskCaptor.getAllValues();
        assertEquals(TaskStatus.PROCESSED, savedTasks.get(0).getStatus());
        assertEquals(TaskStatus.COMPLETED, savedTasks.get(1).getStatus());
        verify(continueFlagRepository).save(flag);
    }

    @Test
    @DisplayName("UT: deletePastIntervalBatch() when client returns empty list should set shouldContinue to false")
    void deletePastIntervalBatch_whenClientReturnsEmptyList_shouldSetShouldContinueToFalse() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(20);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.deleteIntervalsBatchBeforeWeakStart(20)).thenReturn(Collections.emptyList());

        // when
        service.deletePastIntervalBatch();

        // then
        assertFalse(flag.isShouldContinue());
        verify(continueFlagRepository).save(flag);
    }

    @Test
    @DisplayName("UT: deletePastIntervalBatch() when client throws exception should set task status to error")
    void deletePastIntervalBatch_whenClientThrowsException_shouldSetTaskStatusToError() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(20);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.deleteIntervalsBatchBeforeWeakStart(20)).thenThrow(new RuntimeException("API Error"));

        // when & then
        assertThrows(RuntimeException.class, () -> service.deletePastIntervalBatch());
        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(2)).save(taskCaptor.capture());
        assertEquals(TaskStatus.ERROR, taskCaptor.getValue().getStatus());
    }

    @Test
    @DisplayName("UT: markPastAppointmentBatchSkipped() when flag not found should throw exception")
    void markPastAppointmentBatchSkipped_whenFlagNotFound_shouldThrowException() {
        // given
        when(continueFlagRepository.findByType(TaskType.MARK_APPOINTMENT_BATCH_SKIP)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContinueFlagNotFoundByTypeException.class, () -> service.markPastAppointmentBatchSkipped());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: markPastAppointmentBatchSkipped() when shouldContinue is false should skip")
    void markPastAppointmentBatchSkipped_whenShouldContinueIsFalse_shouldSkip() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(false);
        when(continueFlagRepository.findByType(TaskType.MARK_APPOINTMENT_BATCH_SKIP)).thenReturn(Optional.of(flag));

        // when
        service.markPastAppointmentBatchSkipped();

        // then
        verify(restClient, never()).markAppointmentBatchAsSkipped(anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: markPastAppointmentBatchSkipped() when client returns items should complete task")
    void markPastAppointmentBatchSkipped_whenClientReturnsItems_shouldCompleteTask() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(20);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.MARK_APPOINTMENT_BATCH_SKIP)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.markAppointmentBatchAsSkipped(20)).thenReturn(List.of(1L, 2L));

        // when
        service.markPastAppointmentBatchSkipped();

        // then
        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(2)).save(taskCaptor.capture());
        List<TaskEntity> savedTasks = taskCaptor.getAllValues();
        assertEquals(TaskStatus.PROCESSED, savedTasks.get(0).getStatus());
        assertEquals(TaskStatus.COMPLETED, savedTasks.get(1).getStatus());
        verify(continueFlagRepository).save(flag);
    }
}
