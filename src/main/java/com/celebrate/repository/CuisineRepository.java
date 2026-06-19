package com.celebrate.repository;

import com.celebrate.entity.CuisineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuisineRepository extends JpaRepository<CuisineEntity, String> {

    List<CuisineEntity> findByShopType(String shopType);

    java.util.Optional<CuisineEntity> findByName(String name);

    @Query("SELECT c FROM CuisineEntity c WHERE " +
           "(:shopType IS NULL OR c.shopType = :shopType) " +
           "AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<CuisineEntity> findAllFiltered(@Param("shopType") String shopType,
                                         @Param("keyword") String keyword,
                                         Pageable pageable);
}
