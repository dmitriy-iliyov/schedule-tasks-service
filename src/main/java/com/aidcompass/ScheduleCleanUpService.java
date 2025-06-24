package com.aidcompass;

import com.aidcompass.exceptions.ContinueFlagNotFoundByTypeException;
import com.aidcompass.models.entity.ContinueFlagEntity;
import com.aidcompass.models.entity.TaskEntity;
import com.aidcompass.models.TaskStatus;
import com.aidcompass.task_type.models.TaskType;
import com.aidcompass.task_type.models.TaskTypeEntity;
import com.aidcompass.repositories.*;
import com.aidcompass.task_type.TaskTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleCleanUpService {

    //private final AppointmentRepository appointmentRepository;
    private final IntervalRepository intervalRepository;
    private final TaskRepository taskRepository;
    private final TaskTypeService taskTypeService;
    private final ContinueFlagRepository continueFlagRepository;


    @Transactional
    public void deletePastIntervalBatch(int batchSize) {
        TaskTypeEntity taskTypeEntity = taskTypeService.findByTaskType(TaskType.DELETE_INTERVAL);
        ContinueFlagEntity continueFlagEntity = continueFlagRepository.findByTypeEntity(taskTypeEntity).orElseThrow(
                ContinueFlagNotFoundByTypeException::new
        );
        if (continueFlagEntity.isShouldContinue()) {
            try {
                TaskEntity taskEntity = new TaskEntity(
                        taskTypeEntity,
                        TaskStatus.NOT_COMPLETED,
                        batchSize,
                        Instant.now(),
                        null
                );
                taskEntity = taskRepository.save(taskEntity);
                try {
                    LocalDate weakStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    List<Long> deletedIds = intervalRepository.deleteBatchBeforeDate(batchSize, weakStart);

                    if (deletedIds.isEmpty()) {
                        continueFlagEntity.setShouldContinue(false);
                        continueFlagRepository.save(continueFlagEntity);
                    }

                    taskEntity.setStatus(TaskStatus.COMPLETED);
                    taskEntity.setEnd(Instant.now());
                } catch (Exception e) {
                    taskEntity.setStatus(TaskStatus.ERROR);
                    taskEntity.setEnd(Instant.now());
                }
                taskRepository.save(taskEntity);
                return;
            } catch (Exception e) {
                log.error("Error when saving start TaskEntity: {}", (Object[]) e.getStackTrace());
            }
        }
        log.info("shouldContinue: {}", continueFlagEntity.isShouldContinue());
    }

    // переводить в новое состояние записи на которые не появился человек
    public void markPastAppointmentBatchSkipped(int batchSize) {
        //List<Long> ids = repository.deleteBatchByDate(batchSize, date);
        //log.error("Deleted appointment id list: {}", ids);
    }

    public void markAppointmentBatchSkipped(int batchSize) {

    }
}
