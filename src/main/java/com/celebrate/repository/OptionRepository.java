package com.celebrate.repository;

import com.celebrate.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, String> {

    List<OptionEntity> findByRestaurantId(String restaurantId);

    List<OptionEntity> findByIdIn(List<String> ids);
}
