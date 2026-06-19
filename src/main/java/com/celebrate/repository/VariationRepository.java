package com.celebrate.repository;

import com.celebrate.entity.VariationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationRepository extends JpaRepository<VariationEntity, String> {

    List<VariationEntity> findByFoodId(String foodId);

    void deleteByFoodId(String foodId);
}
