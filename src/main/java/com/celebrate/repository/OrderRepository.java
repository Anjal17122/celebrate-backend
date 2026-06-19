package com.celebrate.repository;

import com.celebrate.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    Optional<OrderEntity> findByOrderId(String orderId);

    Page<OrderEntity> findByUserId(String userId, Pageable pageable);

    List<OrderEntity> findByUserId(String userId);

    Page<OrderEntity> findByRestaurantId(String restaurantId, Pageable pageable);

    List<OrderEntity> findByRestaurantId(String restaurantId);

    List<OrderEntity> findByRiderId(String riderId);

    List<OrderEntity> findByZoneId(String zoneId);

    List<OrderEntity> findByRiderIsNull();

    @Query("SELECT o FROM OrderEntity o WHERE o.orderStatus NOT IN ('DELIVERED', 'CANCELLED', 'CANCELLEDBYREST')")
    List<OrderEntity> findUndeliveredOrders();

    @Query("SELECT o FROM OrderEntity o WHERE o.orderStatus IN ('DELIVERED')")
    List<OrderEntity> findDeliveredOrders();

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.id = :restaurantId " +
           "AND (:search IS NULL OR LOWER(o.orderId) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY o.createdAt DESC")
    Page<OrderEntity> findByRestaurantWithSearch(@Param("restaurantId") String restaurantId,
                                                  @Param("search") String search,
                                                  Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE " +
           "(:orderStatus IS NULL OR o.orderStatus IN :orderStatus) " +
           "AND (:search IS NULL OR LOWER(o.orderId) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY o.createdAt DESC")
    Page<OrderEntity> findAllPaginated(@Param("orderStatus") List<String> orderStatus,
                                        @Param("search") String search,
                                        Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.id = :restaurantId " +
           "AND o.orderStatus IN ('PENDING', 'PREPARING', 'PICKED', 'ASSIGNED')")
    List<OrderEntity> findActiveByRestaurantId(@Param("restaurantId") String restaurantId);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.restaurant.id = :restaurantId")
    int countByRestaurantId(@Param("restaurantId") String restaurantId);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<OrderEntity> findOrdersByUserIdWithPagination(
            @Param("userId") String userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
