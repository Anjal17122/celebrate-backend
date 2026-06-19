package com.celebrate.repository;

import com.celebrate.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, String> {

    List<FoodEntity> findByCategoryId(String categoryId);

    List<FoodEntity> findByIdIn(List<String> ids);

    @Query("SELECT f FROM FoodEntity f WHERE f.category.id = :categoryId " +
           "AND (:inStock IS NULL OR f.isOutOfStock = false) " +
           "AND (:search IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<FoodEntity> findByCategoryFiltered(@Param("categoryId") String categoryId,
                                             @Param("inStock") Boolean inStock,
                                             @Param("search") String search);

    @Query("SELECT f FROM FoodEntity f JOIN UserEntity u ON :userId MEMBER OF u.favourite WHERE f.id = f.id")
    List<FoodEntity> findLikedByUserId(@Param("userId") String userId);

    List<FoodEntity> findBySubCategoryId(String subCategoryId);
}
