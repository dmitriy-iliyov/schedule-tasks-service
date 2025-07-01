package com.aidcompass.models.entity;

import com.aidcompass.models.enums.TaskType;
import com.aidcompass.models.enums.TaskTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "continue_flags")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContinueFlagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_type_code", nullable = false, unique = true)
    @Convert(converter = TaskTypeConverter.class)
    private TaskType type;

    @Column(name = "should_continue", nullable = false)
    private boolean shouldContinue;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "page_number")
    private int page;

    public ContinueFlagEntity(TaskType type, boolean shouldContinue, int batchSize) {
        this.type = type;
        this.shouldContinue = shouldContinue;
        this.batchSize = batchSize;
    }

    public ContinueFlagEntity(TaskType type, boolean shouldContinue, int batchSize, int page) {
        this.type = type;
        this.shouldContinue = shouldContinue;
        this.batchSize = batchSize;
        this.page = page;
    }
}
