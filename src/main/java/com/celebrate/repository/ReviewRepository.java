package com.celebrate.repository;

import com.celebrate.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String> {

    List<ReviewEntity> findByRestaurantId(String restaurantId);

    List<ReviewEntity> findByRestaurantId(String restaurantId, Pageable pageable);

    Optional<ReviewEntity> findByOrderId(String orderId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") String restaurantId);

    long countByRestaurantId(String restaurantId);
}
