package com.aidcompass;

import com.aidcompass.models.dto.ContinueFlagDto;
import com.aidcompass.services.ContinueFlagService;
import com.aidcompass.services.ScheduleCleanUpService;
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
}
