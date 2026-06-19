package com.celebrate.repository;

import com.celebrate.entity.TippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TippingRepository extends JpaRepository<TippingEntity, String> {

    Optional<TippingEntity> findFirstBy();
}
