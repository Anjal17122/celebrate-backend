package com.celebrate.repository;

import com.celebrate.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, String> {

    Optional<CouponEntity> findByTitleAndRestaurantIsNull(String title);

    Optional<CouponEntity> findByTitleAndRestaurantId(String title, String restaurantId);

    List<CouponEntity> findByRestaurantIsNull();

    List<CouponEntity> findByRestaurantId(String restaurantId);
}
