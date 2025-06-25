package com.aidcompass.services;

import com.aidcompass.exceptions.models.ContinueFlagNotFoundByTypeException;
import com.aidcompass.models.entity.ContinueFlagEntity;
import com.aidcompass.models.entity.TaskEntity;
import com.aidcompass.models.enums.TaskStatus;
import com.aidcompass.models.enums.TaskType;
import com.aidcompass.repositories.ContinueFlagRepository;
import com.aidcompass.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleNotificationService {

    private final ContinueFlagRepository continueFlagRepository;
    private final TaskRepository taskRepository;


    @Transactional
    public void notifyBatchBeforeAppointment() {
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)
                .orElseThrow(ContinueFlagNotFoundByTypeException::new);
        if (continueFlagEntity.isShouldContinue()) {
            int batchSize = continueFlagEntity.getBatchSize();
            TaskEntity taskEntity = new TaskEntity(
                    TaskType.NOTIFY_BEFORE_APPOINTMENT,
                    TaskStatus.NOT_COMPLETED,
                    batchSize,
                    Instant.now(),
                    null
            );
            taskRepository.save(taskEntity);

            //List<> appointments = appointmentRepository.
        }
        log.info("shouldContinue flag: {}", continueFlagEntity.isShouldContinue());
    }
}
