package com.aidcompass.task_type;

import com.aidcompass.exceptions.TaskTypeNotFoundByTypeException;
import com.aidcompass.task_type.models.TaskType;
import com.aidcompass.task_type.models.TaskTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TaskTypeService {

    private final TaskTypeRepository repository;
    private Map<TaskType, TaskTypeEntity> cache;


    @Transactional
    public void setUpCache() {
        cache = new ConcurrentHashMap<>();
        for (TaskType type: TaskType.values()) {
            cache.put(type, repository.findByType(type).orElseThrow(TaskTypeNotFoundByTypeException::new));
        }
    }

    @Transactional
    public TaskTypeEntity findByTaskType(TaskType type) {
        TaskTypeEntity taskTypeEntity = cache.get(type);
        if (taskTypeEntity == null) {
            taskTypeEntity = repository.findByType(type).orElseThrow(
                    TaskTypeNotFoundByTypeException::new
            );
            cache.put(TaskType.DELETE_INTERVAL, taskTypeEntity);
        }
        return taskTypeEntity;
    }
}
