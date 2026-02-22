package com.aidcompass.schedule_task_service.exceptions.models;


import com.aidcompass.schedule_task_service.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public class TaskStatusNotFoundByCodeException extends BaseNotFoundException {

    private final ErrorDto errorDto = new ErrorDto("task_status", "Task status not found by code!");


    @Override
    public ErrorDto getErrorDto() {
        return this.errorDto;
    }
}
