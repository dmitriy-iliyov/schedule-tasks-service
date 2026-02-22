package com.aidcompass.schedule_task_service.exceptions;

import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.method.ParameterValidationResult;

import java.util.*;

@UtilityClass
public class ErrorUtils {

    public List<ErrorDto> toErrorDtoList(@NotNull BindingResult bindingResult) {
        List<ErrorDto> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> {
            if (!Objects.requireNonNull(error.getDefaultMessage()).isBlank()) {
                errors.add(new ErrorDto(error.getField(), Objects.requireNonNull(error.getDefaultMessage())));
            }
        });
        Collections.reverse(errors);
        return errors;
    }

    public List<ErrorDto> toErrorDtoList(List<ParameterValidationResult> validationResult) {
        List<ErrorDto> errors = new ArrayList<>();
        validationResult.forEach(result -> {
            result.getResolvableErrors().forEach(error -> {
                if (!Objects.requireNonNull(error.getDefaultMessage()).isBlank()) {
                    String[] parts = Objects.requireNonNull(error.getCodes())[0].split("\\.");
                    if (!error.getDefaultMessage().isBlank()) {
                        errors.add(new ErrorDto(parts[parts.length - 1], error.getDefaultMessage()));
                    }
                }
            });
        });
        Collections.reverse(errors);
        return errors;
    }

    public List<ErrorDto> toErrorDtoList(@NotNull Set<ConstraintViolation<?>> bindingResult) {
        List<ErrorDto> errors = new ArrayList<>();
        for (ConstraintViolation<?> item : bindingResult)
            if (!item.getMessage().isBlank()) {
                errors.add(new ErrorDto(item.getPropertyPath().toString(), item.getMessage()));
            }
        Collections.reverse(errors);
        return errors;
    }
}
