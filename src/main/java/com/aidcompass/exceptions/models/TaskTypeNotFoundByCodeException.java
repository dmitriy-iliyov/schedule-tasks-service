package com.aidcompass.exceptions.models;


import com.aidcompass.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

public class TaskTypeNotFoundByCodeException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_type", "Not found task type by code!");
    }
}
