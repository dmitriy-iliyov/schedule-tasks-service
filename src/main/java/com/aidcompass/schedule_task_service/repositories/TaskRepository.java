package com.aidcompass.schedule_task_service.repositories;

import com.aidcompass.schedule_task_service.models.entity.TaskEntity;
import com.aidcompass.schedule_task_service.models.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByType(TaskType typeEntity);
}
