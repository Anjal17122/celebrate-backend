package com.celebrate.repository;

import com.celebrate.entity.WithdrawRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequestEntity, String> {

    List<WithdrawRequestEntity> findByRiderId(String riderId);

    List<WithdrawRequestEntity> findByStoreId(String storeId);

    Optional<WithdrawRequestEntity> findTopByRiderIdAndStatusOrderByCreatedAtDesc(String riderId, String status);

    Optional<WithdrawRequestEntity> findTopByStoreIdAndStatusOrderByCreatedAtDesc(String storeId, String status);

    @Query("SELECT w FROM WithdrawRequestEntity w WHERE " +
           "(:userType = 'RIDER' AND w.rider.id = :userId) OR " +
           "(:userType = 'STORE' AND w.store.id = :userId)")
    Page<WithdrawRequestEntity> findByUserTypeAndUserId(@Param("userType") String userType,
                                                         @Param("userId") String userId,
                                                         Pageable pageable);
}
