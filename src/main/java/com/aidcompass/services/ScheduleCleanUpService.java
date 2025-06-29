package com.aidcompass.services;

import com.aidcompass.exceptions.models.ContinueFlagNotFoundByTypeException;
import com.aidcompass.models.entity.ContinueFlagEntity;
import com.aidcompass.models.entity.TaskEntity;
import com.aidcompass.models.enums.TaskStatus;
import com.aidcompass.models.enums.TaskType;
import com.aidcompass.repositories.ContinueFlagRepository;
import com.aidcompass.repositories.TaskRepository;
import com.aidcompass.rest_client.ScheduleSystemRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleCleanUpService {

    private final TaskRepository taskRepository;
    private final ContinueFlagRepository continueFlagRepository;
    private final ScheduleSystemRestClient restClient;


    @Transactional
    public void deletePastIntervalBatch() {
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.DELETE_INTERVAL).orElseThrow(
                ContinueFlagNotFoundByTypeException::new
        );
        int batchSize = continueFlagEntity.getBatchSize();
        if (continueFlagEntity.isShouldContinue()) {
            TaskEntity taskEntity = new TaskEntity(
                    TaskType.DELETE_INTERVAL,
                    TaskStatus.NOT_COMPLETED,
                    batchSize,
                    Instant.now(),
                    null
            );
            taskEntity = taskRepository.save(taskEntity);
            try {

                List<Long> deletedIds = restClient.deleteIntervalsBatchBeforeWeakStart(batchSize);

                if (deletedIds.isEmpty() || deletedIds.size() < batchSize) {
                    continueFlagEntity.setShouldContinue(false);
                    continueFlagRepository.save(continueFlagEntity);
                }

                taskEntity.setStatus(TaskStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error when deleting intervals: {}", (Object[]) e.getStackTrace());
                taskEntity.setStatus(TaskStatus.ERROR);
                throw e;
            }
            taskEntity.setEnd(Instant.now());
            taskRepository.save(taskEntity);
            return;
        }
        log.info("shouldContinue={}", false);
    }

    @Transactional
    public void markPastAppointmentBatchSkipped() {
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.MARK_APPOINTMENT_SKIPPED)
                .orElseThrow(ContinueFlagNotFoundByTypeException::new);
        if (continueFlagEntity.isShouldContinue()) {
            int batchSize = continueFlagEntity.getBatchSize();
            try {
                TaskEntity taskEntity = new TaskEntity(
                        TaskType.MARK_APPOINTMENT_SKIPPED,
                        TaskStatus.NOT_COMPLETED,
                        batchSize,
                        Instant.now(),
                        null
                );
                taskRepository.save(taskEntity);

                try {

                    List<Long> deletedIds = restClient.markAppointmentBatchAsSkipped(batchSize);

                    if (deletedIds.isEmpty() || deletedIds.size() < batchSize) {
                        continueFlagEntity.setShouldContinue(false);
                        continueFlagRepository.save(continueFlagEntity);
                    }

                    taskEntity.setStatus(TaskStatus.COMPLETED);
                } catch (Exception e) {
                    log.error("Error when executing task: {}", (Object[]) e.getStackTrace());
                    taskEntity.setStatus(TaskStatus.ERROR);
                }
                taskEntity.setEnd(Instant.now());
                taskRepository.save(taskEntity);
            } catch(Exception e) {
                log.error("Error when start task: {}", (Object[]) e.getStackTrace());
            }
        }
    }
}
