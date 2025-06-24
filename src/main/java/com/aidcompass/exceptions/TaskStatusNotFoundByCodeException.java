package com.aidcompass.exceptions;

import com.aidcompass.models.BaseNotFoundException;
import com.aidcompass.models.dto.ErrorDto;

public class TaskStatusNotFoundByCodeException extends BaseNotFoundException {

    private final ErrorDto errorDto = new ErrorDto("task_status", "Task status not found by code!");


    @Override
    public ErrorDto getErrorDto() {
        return this.errorDto;
    }
}
