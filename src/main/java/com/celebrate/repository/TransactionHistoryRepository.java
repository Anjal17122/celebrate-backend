package com.celebrate.repository;

import com.celebrate.entity.TransactionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, String> {

    @Query("SELECT t FROM TransactionHistoryEntity t WHERE " +
           "(:userType IS NULL OR t.userType = :userType) " +
           "AND (:userId IS NULL OR t.userId = :userId) " +
           "AND (:search IS NULL OR LOWER(t.transactionId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<TransactionHistoryEntity> findFiltered(@Param("userType") String userType,
                                                 @Param("userId") String userId,
                                                 @Param("search") String search,
                                                 Pageable pageable);
}
