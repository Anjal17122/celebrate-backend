package com.celebrate.repository;

import com.celebrate.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, String> {

    List<ZoneEntity> findByIsActiveTrue();
}
