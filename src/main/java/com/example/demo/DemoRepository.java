package com.example.demo;

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
        int setStatus(
                        @Param("id") Long id,
                        @Param("status") ReservationStatus status);

}
