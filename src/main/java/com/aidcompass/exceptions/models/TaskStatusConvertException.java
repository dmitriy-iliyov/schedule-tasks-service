package com.aidcompass.exceptions.models;


import com.aidcompass.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

public class TaskStatusConvertException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_status", "Converter exception");
    }
}
