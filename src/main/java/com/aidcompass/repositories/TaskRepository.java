package com.aidcompass.repositories;

import com.aidcompass.models.entity.TaskEntity;
import com.aidcompass.task_type.models.TaskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTypeEntity(TaskTypeEntity typeEntity);
}
