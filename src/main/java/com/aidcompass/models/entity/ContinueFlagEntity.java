package com.aidcompass.models.entity;

import com.aidcompass.task_type.models.TaskTypeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_type_id", nullable = false)
    private TaskTypeEntity typeEntity;

    @Column(name = "should_continue", nullable = false)
    private boolean shouldContinue;
}
