package com.celebrate.repository;

import com.celebrate.entity.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<RiderEntity, String> {

    Optional<RiderEntity> findByUsername(String username);

    List<RiderEntity> findByZoneId(String zoneId);

    List<RiderEntity> findByAvailableTrueAndIsActiveTrue();

    List<RiderEntity> findByAvailableTrue();

    boolean existsByUsername(String username);

    long count();
}
