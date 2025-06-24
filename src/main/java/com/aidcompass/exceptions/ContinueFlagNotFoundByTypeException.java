package com.aidcompass.exceptions;

import com.aidcompass.models.BaseNotFoundException;
import com.aidcompass.models.dto.ErrorDto;

public class ContinueFlagNotFoundByTypeException extends BaseNotFoundException {
    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("continue_flag", "Not found by type!");
    }
}
