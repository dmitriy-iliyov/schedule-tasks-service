package com.aidcompass.schedule_task_service.exceptions.models.dto;

public record ExceptionResponseDto(
        String code,
        String message,
        String description
) { }
