package com.example.demo.reservations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemoRepository extends JpaRepository<DemoEntity, Long> {

        @Modifying
        @Query("delete DemoEntity r where r.status = CANCELLED")
        void deleteAllCanceledEntity();

        @Modifying
        @Query("""
                        update DemoEntity r
                        set r.status = :status
                        where r.id = :id
                        """)
        void setStatus(
                        @Param("id") Long id,
                        @Param("status") ReservationStatus status);

        @Query("""
                        SELECT r.id from DemoEntity r
                        WHERE r.roomId = :roomId
                        AND :startDate < r.endDate
                        AND r.startDate < :endDate
                        AND r.status = :status
                                """)
        List<Long> findConflictReservationIds(
                        @Param("roomId") Long roomId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("status") ReservationStatus status);

        @Query("""
                        SELECT r from DemoEntity r
                        WHERE (:roomId IS NULL OR r.roomId = :roomId)
                        AND (:userId IS NULL OR r.userId = :userId)
                        """)
        List<DemoEntity> searchByFilter(
                        @Param("roomId") Long roomId,
                        @Param("userId") Long userId,
                        Pageable pageable);
}
