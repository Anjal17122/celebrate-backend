package com.celebrate.repository;

import com.celebrate.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, String> {

    List<AddressEntity> findByUserId(String userId);

    void deleteByIdInAndUserId(List<String> ids, String userId);
}
