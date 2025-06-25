package com.aidcompass.exceptions.models;


import com.aidcompass.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.exceptions.models.dto.ErrorDto;

public class ContinueFlagNotFoundByTypeException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("continue_flag", "Not found by type!");
    }
}
