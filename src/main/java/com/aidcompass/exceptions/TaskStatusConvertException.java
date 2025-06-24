package com.aidcompass.exceptions;

import com.aidcompass.models.BaseNotFoundException;
import com.aidcompass.models.dto.ErrorDto;

public class TaskStatusConvertException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_status", "Converter exception");
    }
}
