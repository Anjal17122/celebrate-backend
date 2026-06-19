package com.celebrate.repository;

import com.celebrate.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, String> {

    Optional<OtpEntity> findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(String email);

    Optional<OtpEntity> findTopByPhoneAndIsUsedFalseOrderByCreatedAtDesc(String phone);
}
