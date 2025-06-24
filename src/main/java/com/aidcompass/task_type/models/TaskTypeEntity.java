package com.aidcompass.task_type.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_types")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private TaskType type;

    public TaskTypeEntity(TaskType type) {
        this.type = type;
    }
}
