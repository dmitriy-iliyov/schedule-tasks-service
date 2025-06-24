package com.aidcompass;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/v1/schedule")
@RequiredArgsConstructor
public class SystemScheduleController {

    private final ScheduleCleanUpService cleanUpService;


    @DeleteMapping("/past-interval")
    public ResponseEntity<?> deletePastIntervalBatch(@RequestParam("batchSize")
                                                     @Positive(message = "Batch size should be positive!")
                                                     @Min(value = 10, message = "Min batch size is 10!")
                                                     int batchSize) {
        cleanUpService.deletePastIntervalBatch(batchSize);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/mark-appointment-skipped")
    public ResponseEntity<?> markAppointmentBatchSkipped(@RequestParam("batchSize")
                                                         @Positive(message = "Batch size should be positive!")
                                                         @Min(value = 10, message = "Min batch size is 10!")
                                                         int batchSize) {
        cleanUpService.deletePastIntervalBatch(batchSize);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
