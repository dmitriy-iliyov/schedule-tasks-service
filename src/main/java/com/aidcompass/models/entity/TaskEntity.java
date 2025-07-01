package com.aidcompass.models.entity;

import com.aidcompass.models.enums.TaskStatus;
import com.aidcompass.models.enums.TaskStatusConverter;
import com.aidcompass.models.enums.TaskType;
import com.aidcompass.models.enums.TaskTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
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

    @Column(name = "task_type_code", nullable = false)
    @Convert(converter = TaskTypeConverter.class)
    private TaskType type;

    @Column(name = "task_status_code", nullable = false)
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "page_number")
    private int page;

    @Column(name = "start_t", nullable = false)
    private Instant start;

    @Column(name = "end_t")
    private Instant end;

    @Column(name = "executionTime")
    private long executionTime;

    public TaskEntity(TaskType type, TaskStatus taskStatus, int batchSize, Instant start, Instant end) {
        this.type = type;
        this.status = taskStatus;
        this.batchSize = batchSize;
        this.start = start;
        this.end = end;
    }

    public TaskEntity(TaskType type, TaskStatus taskStatus, int batchSize, int page, Instant start, Instant end) {
        this.type = type;
        this.status = taskStatus;
        this.batchSize = batchSize;
        this.page = page;
        this.start = start;
        this.end = end;
    }

    @PreUpdate
    public void preUpdate() {
        if (this.end != null) {
            this.executionTime = Duration.between(start, end).toSeconds();
        } else {
            this.executionTime = Duration.between(start, Instant.now()).toSeconds();
        }
    }
}
