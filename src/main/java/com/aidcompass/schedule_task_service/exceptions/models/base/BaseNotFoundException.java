package com.aidcompass.schedule_task_service.exceptions.models.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseNotFoundException extends Exception {

    private final static String MESSAGE = "Not found";
    private final String code = "";


    public BaseNotFoundException() {
        super(MESSAGE);
    }

    public BaseNotFoundException(String message) {
        super(MESSAGE + ": " + message);
    }
}
