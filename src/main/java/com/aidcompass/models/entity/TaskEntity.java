package com.aidcompass.models.entity;

import com.aidcompass.models.TaskStatus;
import com.aidcompass.models.TaskStatusConverter;
import com.aidcompass.task_type.models.TaskTypeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "task_journal")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_type_id", nullable = false)
    private TaskTypeEntity typeEntity;

    @Column(name = "task_status_code", nullable = false)
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "start_t", nullable = false)
    private Instant start;

    @Column(name = "end_t")
    private Instant end;

    public TaskEntity(TaskTypeEntity typeEntity, TaskStatus taskStatus, int batchSize, Instant start, Instant end) {
        this.typeEntity = typeEntity;
        this.status = taskStatus;
        this.batchSize = batchSize;
        this.start = start;
        this.end = end;
    }
}
