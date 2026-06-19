package com.celebrate.repository;

import com.celebrate.entity.DayScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DayScheduleRepository extends JpaRepository<DayScheduleEntity, String> {

    @Modifying
    @Query("DELETE FROM DayScheduleEntity d WHERE d.rider.id = :riderId")
    void deleteByRiderId(@Param("riderId") String riderId);
}
