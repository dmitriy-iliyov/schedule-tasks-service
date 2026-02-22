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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleNotificationServiceUnitTests {

    @Mock
    private ContinueFlagRepository continueFlagRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ScheduleSystemRestClient restClient;

    @InjectMocks
    private ScheduleNotificationService service;

    @Test
    @DisplayName("UT: notifyBatchBeforeAppointment() when flag not found should throw exception")
    void notifyBatchBeforeAppointment_whenFlagNotFound_shouldThrowException() {
        // given
        when(continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContinueFlagNotFoundByTypeException.class, () -> service.notifyBatchBeforeAppointment());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: notifyBatchBeforeAppointment() when shouldContinue is false should skip")
    void notifyBatchBeforeAppointment_whenShouldContinueIsFalse_shouldSkip() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(false);
        when(continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)).thenReturn(Optional.of(flag));

        // when
        service.notifyBatchBeforeAppointment();

        // then
        verify(restClient, never()).notifyBatch(anyInt(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT: notifyBatchBeforeAppointment() when client returns true should complete task and increment page")
    void notifyBatchBeforeAppointment_whenClientReturnsTrue_shouldCompleteTaskAndIncrementPage() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(10);
        flag.setPage(5);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.notifyBatch(10, 5)).thenReturn(true);

        // when
        service.notifyBatchBeforeAppointment();

        // then
        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(2)).save(taskCaptor.capture());
        assertEquals(TaskStatus.COMPLETED, taskCaptor.getValue().getStatus());
        assertEquals(6, taskCaptor.getValue().getPage());

        ArgumentCaptor<ContinueFlagEntity> flagCaptor = ArgumentCaptor.forClass(ContinueFlagEntity.class);
        verify(continueFlagRepository).save(flagCaptor.capture());
        assertEquals(6, flagCaptor.getValue().getPage());
        assertTrue(flagCaptor.getValue().isShouldContinue());
    }

    @Test
    @DisplayName("UT: notifyBatchBeforeAppointment() when client returns false should set shouldContinue to false")
    void notifyBatchBeforeAppointment_whenClientReturnsFalse_shouldSetShouldContinueToFalse() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(10);
        flag.setPage(5);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.notifyBatch(10, 5)).thenReturn(false);

        // when
        service.notifyBatchBeforeAppointment();

        // then
        ArgumentCaptor<ContinueFlagEntity> flagCaptor = ArgumentCaptor.forClass(ContinueFlagEntity.class);
        verify(continueFlagRepository).save(flagCaptor.capture());
        assertFalse(flagCaptor.getValue().isShouldContinue());
    }

    @Test
    @DisplayName("UT: notifyBatchBeforeAppointment() when client throws exception should set task status to error")
    void notifyBatchBeforeAppointment_whenClientThrowsException_shouldSetTaskStatusToError() {
        // given
        ContinueFlagEntity flag = new ContinueFlagEntity();
        flag.setShouldContinue(true);
        flag.setBatchSize(10);
        flag.setPage(5);
        TaskEntity task = new TaskEntity();
        when(continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)).thenReturn(Optional.of(flag));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        when(restClient.notifyBatch(10, 5)).thenThrow(new RuntimeException("API Error"));

        // when
        service.notifyBatchBeforeAppointment();

        // then
        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(2)).save(taskCaptor.capture());
        assertEquals(TaskStatus.ERROR, taskCaptor.getValue().getStatus());
    }
}
