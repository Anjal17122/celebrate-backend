package com.celebrate.repository;

import com.celebrate.entity.WebNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebNotificationRepository extends JpaRepository<WebNotificationEntity, String> {

    List<WebNotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Modifying
    @Query("UPDATE WebNotificationEntity w SET w.read = true WHERE w.user.id = :userId")
    void markAllAsReadByUserId(@Param("userId") String userId);
}
