package com.aidcompass.models.enums;

import com.aidcompass.exceptions.models.TaskTypeNotFoundByCodeException;
import lombok.Getter;

import java.util.Arrays;

public enum TaskType {
    DELETE_INTERVAL(0),
    MARK_APPOINTMENT_SKIPPED(1),
    NOTIFY_BEFORE_APPOINTMENT(2);

    @Getter
    private final int code;

    TaskType(int code) {
        this.code = code;
    }

    public static TaskType fromCode(int code) {
        return Arrays.stream(TaskType.values())
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElseThrow(TaskTypeNotFoundByCodeException::new);
    }
}
