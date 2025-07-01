package com.aidcompass.models.enums;


import com.aidcompass.exceptions.models.TaskStatusNotFoundByCodeException;

import java.util.Arrays;

public enum TaskStatus {
    PROCESSED(0),
    COMPLETED(1),
    ERROR(2);

    private final int code;

    TaskStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TaskStatus fromCode(int code) {
        return Arrays.stream(TaskStatus.values())
                .filter(taskStatus -> taskStatus.getCode() == code)
                .findFirst()
                .orElseThrow(TaskStatusNotFoundByCodeException::new);
    }
}
