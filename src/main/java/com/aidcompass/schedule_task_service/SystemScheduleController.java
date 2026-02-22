package com.aidcompass.schedule_task_service;

import com.aidcompass.schedule_task_service.models.dto.ContinueFlagDto;
import com.aidcompass.schedule_task_service.services.ContinueFlagService;
import com.aidcompass.schedule_task_service.services.ScheduleCleanUpService;
import com.aidcompass.schedule_task_service.services.ScheduleNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-scheduler/v1")
@RequiredArgsConstructor
public class SystemScheduleController {

    private final ScheduleCleanUpService cleanUpService;
    private final ScheduleNotificationService notificationService;
    private final ContinueFlagService continueFlagService;


    @PutMapping("/flag-config")
    public ResponseEntity<?> updateContinueFlag(@RequestBody @Valid ContinueFlagDto dto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(continueFlagService.update(dto));
    }

    @DeleteMapping("/intervals/batch")
    public ResponseEntity<?> deletePastIntervalBatch() {
        cleanUpService.deletePastIntervalBatch();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/appointments/batch/skip")
    public ResponseEntity<?> markAppointmentBatchSkipped() {
        cleanUpService.markPastAppointmentBatchSkipped();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/appointments/batch/remind")
    public ResponseEntity<?> remindBatchAppointment() {
        notificationService.notifyBatchBeforeAppointment();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
