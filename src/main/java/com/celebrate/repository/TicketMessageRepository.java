package com.celebrate.repository;

import com.celebrate.entity.TicketMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessageEntity, String> {

    Page<TicketMessageEntity> findByTicketIdOrderByCreatedAtAsc(String ticketId, Pageable pageable);

    Page<TicketMessageEntity> findByTicketId(String ticketId, Pageable pageable);
}
