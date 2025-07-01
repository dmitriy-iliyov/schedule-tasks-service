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
import org.springframework.scheduling.annotation.Scheduled;
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


    @Scheduled(cron = "0 */5 0-3 * * *")
    @Transactional
    public void deletePastIntervalBatch() {
        log.info("START deletePastIntervalBatch");
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.DELETE_INTERVAL_BATCH).orElseThrow(
                ContinueFlagNotFoundByTypeException::new
        );
        if (continueFlagEntity.isShouldContinue()) {
            int batchSize = continueFlagEntity.getBatchSize();
            TaskEntity taskEntity = new TaskEntity(
                    TaskType.DELETE_INTERVAL_BATCH,
                    TaskStatus.PROCESSED,
                    batchSize,
                    Instant.now(),
                    null
            );
            taskEntity = taskRepository.save(taskEntity);
            try {
                List<Long> deletedIds = restClient.deleteIntervalsBatchBeforeWeakStart(batchSize);
                if (deletedIds.isEmpty() || deletedIds.size() < batchSize) {
                    continueFlagEntity.setShouldContinue(false);
                }
                taskEntity.setStatus(TaskStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error when deleting intervals", e);
                taskEntity.setStatus(TaskStatus.ERROR);
                throw e;
            } finally {
                taskEntity.setEnd(Instant.now());
                taskRepository.save(taskEntity);
                continueFlagRepository.save(continueFlagEntity);
                log.info("END deletePastIntervalBatch");
            }
            return;
        }
        log.info("Task {} skipped: shouldContinue=false", TaskType.DELETE_INTERVAL_BATCH);
    }

    @Scheduled(cron = "0 */5 0-4 * * *")
    @Transactional
    public void markPastAppointmentBatchSkipped() {
        log.info("START markPastAppointmentBatchSkipped");
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.MARK_APPOINTMENT_BATCH_SKIP)
                .orElseThrow(ContinueFlagNotFoundByTypeException::new);
        if (continueFlagEntity.isShouldContinue()) {
            int batchSize = continueFlagEntity.getBatchSize();
            TaskEntity taskEntity = new TaskEntity(
                    TaskType.MARK_APPOINTMENT_BATCH_SKIP,
                    TaskStatus.PROCESSED,
                    batchSize,
                    Instant.now(),
                    null
            );
            taskEntity = taskRepository.save(taskEntity);
            try {
                List<Long> deletedIds = restClient.markAppointmentBatchAsSkipped(batchSize);
                if (deletedIds.isEmpty() || deletedIds.size() < batchSize) {
                    continueFlagEntity.setShouldContinue(false);
                }
                taskEntity.setStatus(TaskStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error when executing task: ", e);
                taskEntity.setStatus(TaskStatus.ERROR);
                throw e;
            } finally {
                taskEntity.setEnd(Instant.now());
                taskRepository.save(taskEntity);
                continueFlagRepository.save(continueFlagEntity);
                log.info("END markPastAppointmentBatchSkipped");
            }
            return;
        }
        log.info("Task {} skipped: shouldContinue=false", TaskType.MARK_APPOINTMENT_BATCH_SKIP);
    }
}