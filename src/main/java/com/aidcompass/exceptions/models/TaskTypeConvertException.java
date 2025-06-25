package com.aidcompass.exceptions.models;


import com.aidcompass.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

public class TaskTypeConvertException extends BaseInternalServiceException {
    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_type", "Task type convert exception!");
    }
}
