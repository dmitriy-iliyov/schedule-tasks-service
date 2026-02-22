package com.aidcompass.schedule_task_service.exceptions;

import com.aidcompass.schedule_task_service.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public class ApiException extends BaseInternalServiceException {

    private final String message;


    public ApiException(String message) {
        this.message = message;
    }

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("api", "Api response: " + message);
    }
}
