package com.celebrate.repository;

import com.celebrate.entity.LiveActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveActivityRepository extends JpaRepository<LiveActivityEntity, String> {

    Optional<LiveActivityEntity> findByOrderIdAndActivityId(String orderId, String activityId);
}
