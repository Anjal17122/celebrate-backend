package com.celebrate.repository;

import com.celebrate.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, String> {

    Optional<ConfigurationEntity> findFirstBy();
}
