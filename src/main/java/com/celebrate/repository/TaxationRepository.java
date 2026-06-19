package com.celebrate.repository;

import com.celebrate.entity.TaxationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxationRepository extends JpaRepository<TaxationEntity, String> {

    Optional<TaxationEntity> findFirstBy();
}
