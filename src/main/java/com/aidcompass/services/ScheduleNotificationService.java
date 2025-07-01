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

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleNotificationService {

    private final ContinueFlagRepository continueFlagRepository;
    private final TaskRepository taskRepository;
    private final ScheduleSystemRestClient restClient;


    @Scheduled(cron = "* */5 4-8 * * *")
    @Transactional
    public void notifyBatchBeforeAppointment() {
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByType(TaskType.NOTIFY_BEFORE_APPOINTMENT)
                .orElseThrow(ContinueFlagNotFoundByTypeException::new);
        if (continueFlagEntity.isShouldContinue()) {
            int batchSize = continueFlagEntity.getBatchSize();
            int page = continueFlagEntity.getPage();
            TaskEntity taskEntity = new TaskEntity(
                    TaskType.NOTIFY_BEFORE_APPOINTMENT,
                    TaskStatus.PROCESSED,
                    batchSize,
                    page,
                    Instant.now(),
                    null
            );
            taskEntity = taskRepository.save(taskEntity);

            int nextPage = page + 1;

            try {
                boolean continueNotifying = restClient.notifyBatch(batchSize, page);
                if (!continueNotifying) {
                    continueFlagEntity.setShouldContinue(false);
                }
                taskEntity.setStatus(TaskStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error when notifying: {}", e.getMessage());
                taskEntity.setStatus(TaskStatus.ERROR);
            } finally {
                taskEntity.setEnd(Instant.now());
                taskEntity.setPage(nextPage);
                taskRepository.save(taskEntity);
                continueFlagEntity.setPage(nextPage);
                continueFlagRepository.save(continueFlagEntity);
            }
            return;
        }
        log.info("Task {} skipped: shouldContinue=false", TaskType.NOTIFY_BEFORE_APPOINTMENT);
    }
}
