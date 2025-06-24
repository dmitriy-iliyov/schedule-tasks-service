package com.aidcompass.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IntervalRepository {

    private final EntityManager entityManager;


    @Transactional
    public List<Long> deleteBatchBeforeDate(int batchSize, LocalDate weakStart) {
        return Collections.unmodifiableList(
                entityManager.createNativeQuery("""
                                WITH to_delete AS (
                                    SELECT id
                                    FROM work_intervals
                                    WHERE date < :weak_start
                                    LIMIT :batch_size)
                                DELETE FROM work_intervals
                                WHERE id IN (SELECT id FROM to_delete)
                                RETURNING id
                            """)
                        .setParameter("batch_size", batchSize)
                        .setParameter("week_start", weakStart)
                        .getResultList());
    }
}
