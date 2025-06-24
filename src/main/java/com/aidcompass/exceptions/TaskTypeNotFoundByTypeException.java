package com.aidcompass.exceptions;

import com.aidcompass.models.BaseNotFoundException;
import com.aidcompass.models.dto.ErrorDto;

public class TaskTypeNotFoundByTypeException extends BaseNotFoundException {

    private final ErrorDto errorDto = new ErrorDto("task_type", "Not found by id");


    @Override
    public ErrorDto getErrorDto() {
        return this.errorDto;
    }
}
