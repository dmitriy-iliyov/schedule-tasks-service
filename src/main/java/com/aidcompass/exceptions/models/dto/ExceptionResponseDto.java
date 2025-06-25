package com.aidcompass.exceptions.models.dto;

public record ExceptionResponseDto(
        String code,
        String message,
        String description
) { }
