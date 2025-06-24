package com.aidcompass.task_type;

import com.aidcompass.task_type.models.TaskType;
import com.aidcompass.task_type.models.TaskTypeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskTypeEntity, Integer> {

    @EntityGraph(attributePaths = {"typeEntity"})
    Optional<TaskTypeEntity> findByType(TaskType type);
}
