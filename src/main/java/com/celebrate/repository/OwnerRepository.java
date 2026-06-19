package com.celebrate.repository;

import com.celebrate.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, String> {

    Optional<OwnerEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<OwnerEntity> findByEmailAndUserType(String email, String userType);

    long countByUserType(String userType);
}
