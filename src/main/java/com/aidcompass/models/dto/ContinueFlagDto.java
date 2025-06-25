package com.aidcompass.models.dto;

import com.aidcompass.models.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ContinueFlagDto(
        @JsonProperty("task_type")
        TaskType taskType,
        
        @JsonProperty("batch_size")
        @Positive(message = "Batch size should be positive!")
        @Min(value = 20, message = "Min batch size value is 20!")
        int batchSize,

        @JsonProperty("should_continue")
        boolean shouldContinue
) { }