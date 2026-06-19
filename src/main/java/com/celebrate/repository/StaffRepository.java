package com.celebrate.repository;

import com.celebrate.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, String> {

    Optional<StaffEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
