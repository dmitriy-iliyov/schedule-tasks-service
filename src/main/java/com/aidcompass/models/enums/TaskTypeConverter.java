package com.aidcompass.models.enums;

import com.aidcompass.exceptions.models.TaskTypeConvertException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TaskTypeConverter implements AttributeConverter<TaskType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskType type) {
        if (type == null) {
            throw new TaskTypeConvertException();
        }
        return type.getCode();
    }

    @Override
    public TaskType convertToEntityAttribute(Integer code) {
        if (code == null) {
            throw new TaskTypeConvertException();
        }
        return TaskType.fromCode(code);
    }
}
