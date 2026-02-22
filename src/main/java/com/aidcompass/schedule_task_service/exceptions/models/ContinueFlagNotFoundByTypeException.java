package com.aidcompass.schedule_task_service.exceptions.models;


import com.aidcompass.schedule_task_service.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.schedule_task_service.exceptions.models.dto.ErrorDto;

public class ContinueFlagNotFoundByTypeException extends BaseNotFoundException {

    @Override
    public ErrorDto getErrorDto() {
        return new ErrorDto("continue_flag", "Not found by type!");
    }
}
