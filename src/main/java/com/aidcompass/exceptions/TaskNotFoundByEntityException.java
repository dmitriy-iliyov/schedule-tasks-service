package com.aidcompass.exceptions;

import com.aidcompass.models.BaseNotFoundException;
import com.aidcompass.models.dto.ErrorDto;

public class TaskNotFoundByEntityException extends BaseNotFoundException {

    private final ErrorDto errorDto = new ErrorDto("task", "Not found by type!");


    @Override
    public ErrorDto getErrorDto() {
        return this.errorDto;
    }
}
