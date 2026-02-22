package com.aidcompass.schedule_task_service.exceptions.models;


import com.aidcompass.schedule_task_service.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public class TaskTypeConvertException extends BaseInternalServiceException {
    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_type", "Task type convert exception!");
    }
}
