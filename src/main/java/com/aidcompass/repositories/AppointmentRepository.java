//package com.aidcompass.repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface AppointmentRepository extends JpaRepository<Object, Long> {
//
//    @Modifying
//    @Query(value = """
//        WITH deleted AS (
//            SELECT id FROM appointments
//            WHERE date = :date
//            LIMIT :batch_size
//        )
//        DELETE FROM appointments
//        WHERE id IN (SELECT id FROM deleted)
//        RETURNING id
//    """, nativeQuery = true)
//    List<Long> deleteBatchByDate(@Param("batch_size") int batchSize, @Param("date") LocalDate date);
//}
