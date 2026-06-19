package com.celebrate.repository;

import com.celebrate.entity.SupportTicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicketEntity, String> {

    Page<SupportTicketEntity> findByUserId(String userId, Pageable pageable);

    Optional<SupportTicketEntity> findTopByUserIdOrderByCreatedAtDesc(String userId);

    @Query("SELECT t FROM SupportTicketEntity t WHERE " +
           "(:status IS NULL OR t.status = :status) " +
           "AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "     OR LOWER(t.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<SupportTicketEntity> findAllFiltered(@Param("status") String status,
                                               @Param("search") String search,
                                               Pageable pageable);

    @Query("SELECT t FROM SupportTicketEntity t WHERE t.user.id = :userId " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<SupportTicketEntity> findByUserFiltered(@Param("userId") String userId,
                                                  @Param("status") String status,
                                                  @Param("search") String search,
                                                  Pageable pageable);
}
