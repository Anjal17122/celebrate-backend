package com.celebrate.repository;

import com.celebrate.entity.VersionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionsRepository extends JpaRepository<VersionsEntity, String> {

    Optional<VersionsEntity> findFirstBy();
}
