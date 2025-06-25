package com.aidcompass.repositories;

import com.aidcompass.models.entity.ContinueFlagEntity;
import com.aidcompass.models.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContinueFlagRepository extends JpaRepository<ContinueFlagEntity, Integer> {
    Optional<ContinueFlagEntity> findByType(TaskType type);
}
