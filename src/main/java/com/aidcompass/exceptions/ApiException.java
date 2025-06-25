package com.aidcompass.exceptions;

import com.aidcompass.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

public class ApiException extends BaseInternalServiceException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("api", "Api request produce exception!");
    }
}
