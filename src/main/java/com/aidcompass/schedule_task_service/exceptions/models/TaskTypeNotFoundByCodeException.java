package com.aidcompass.schedule_task_service.exceptions.models;


import com.aidcompass.schedule_task_service.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public class TaskTypeNotFoundByCodeException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("task_type", "Not found task type by code!");
    }
}
