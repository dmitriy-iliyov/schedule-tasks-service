package com.aidcompass.repositories;

import com.aidcompass.models.enums.TaskType;
import com.aidcompass.models.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByType(TaskType typeEntity);
}
