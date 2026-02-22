package com.aidcompass.schedule_task_service.exceptions.models.base;


import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public abstract class Exception extends RuntimeException {


    public Exception() {

    }

    public Exception(String message) {
        super(message);
    }

    abstract public ErrorDto getErrorDto();
}
