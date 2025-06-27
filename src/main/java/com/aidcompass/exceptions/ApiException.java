package com.aidcompass.exceptions;

import com.aidcompass.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

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
