package com.celebrate.repository;

import com.celebrate.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, String> {

    Optional<RestaurantEntity> findBySlug(String slug);

    Optional<RestaurantEntity> findByUsername(String username);

    List<RestaurantEntity> findByOwnerId(String ownerId);

    List<RestaurantEntity> findByZoneId(String zoneId);

    List<RestaurantEntity> findByIsClonedTrue();

    List<RestaurantEntity> findByIsClonedFalseOrIsClonedIsNull();

    @Query("SELECT r FROM RestaurantEntity r WHERE " +
           "(:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RestaurantEntity> findAllWithSearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM RestaurantEntity r WHERE r.isCloned = true AND " +
           "(:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RestaurantEntity> findClonedWithSearch(@Param("search") String search, Pageable pageable);

    long countByOwnerIdIsNotNull();

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.restaurant.id = :restaurantId")
    int countByOrderedItems(@Param("restaurantId") String restaurantId);
}
