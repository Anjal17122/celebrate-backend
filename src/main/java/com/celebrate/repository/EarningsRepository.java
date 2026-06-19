package com.celebrate.repository;

import com.celebrate.entity.EarningsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarningsRepository extends JpaRepository<EarningsEntity, String> {

    List<EarningsEntity> findByRiderId(String riderId);

    Page<EarningsEntity> findByRiderId(String riderId, Pageable pageable);

    default Page<EarningsEntity> findByRiderIdPaginated(String riderId, Pageable pageable) {
        return findByRiderId(riderId, pageable);
    }

    List<EarningsEntity> findByStoreId(String storeId);

    Page<EarningsEntity> findByStoreId(String storeId, Pageable pageable);

    default Page<EarningsEntity> findByStoreIdPaginated(String storeId, Pageable pageable) {
        return findByStoreId(storeId, pageable);
    }

    @Query("SELECT e FROM EarningsEntity e WHERE " +
           "(:userType = 'RIDER' AND e.rider.id = :userId) OR " +
           "(:userType = 'STORE' AND e.store.id = :userId)")
    Page<EarningsEntity> findByUserTypeAndUserId(@Param("userType") String userType,
                                                  @Param("userId") String userId,
                                                  Pageable pageable);
}
