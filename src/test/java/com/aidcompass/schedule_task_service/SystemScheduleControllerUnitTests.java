package com.aidcompass.schedule_task_service;

import com.aidcompass.schedule_task_service.models.dto.ContinueFlagDto;
import com.aidcompass.schedule_task_service.models.enums.TaskType;
import com.aidcompass.schedule_task_service.services.ContinueFlagService;
import com.aidcompass.schedule_task_service.services.ScheduleCleanUpService;
import com.aidcompass.schedule_task_service.services.ScheduleNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemScheduleControllerUnitTests {

    @Mock
    private ScheduleCleanUpService cleanUpService;

    @Mock
    private ScheduleNotificationService notificationService;

    @Mock
    private ContinueFlagService continueFlagService;

    @InjectMocks
    private SystemScheduleController controller;

    @Test
    @DisplayName("UT: updateContinueFlag() should return OK and updated dto")
    void updateContinueFlag_shouldReturnOk() {
        // given
        ContinueFlagDto dto = new ContinueFlagDto(TaskType.DELETE_INTERVAL_BATCH, 50, true);
        when(continueFlagService.update(dto)).thenReturn(dto);

        // when
        ResponseEntity<?> response = controller.updateContinueFlag(dto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(continueFlagService).update(dto);
    }

    @Test
    @DisplayName("UT: deletePastIntervalBatch() should return NO_CONTENT")
    void deletePastIntervalBatch_shouldReturnNoContent() {
        // given

        // when
        ResponseEntity<?> response = controller.deletePastIntervalBatch();

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cleanUpService).deletePastIntervalBatch();
    }

    @Test
    @DisplayName("UT: markAppointmentBatchSkipped() should return NO_CONTENT")
    void markAppointmentBatchSkipped_shouldReturnNoContent() {
        // given

        // when
        ResponseEntity<?> response = controller.markAppointmentBatchSkipped();

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cleanUpService).markPastAppointmentBatchSkipped();
    }

    @Test
    @DisplayName("UT: remindBatchAppointment() should return NO_CONTENT")
    void remindBatchAppointment_shouldReturnNoContent() {
        // given

        // when
        ResponseEntity<?> response = controller.remindBatchAppointment();

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService).notifyBatchBeforeAppointment();
    }
}
