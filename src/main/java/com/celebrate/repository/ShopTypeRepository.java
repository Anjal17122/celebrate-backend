package com.celebrate.repository;

import com.celebrate.entity.ShopTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopTypeRepository extends JpaRepository<ShopTypeEntity, String> {

    Optional<ShopTypeEntity> findByName(String name);

    Optional<ShopTypeEntity> findBySlug(String slug);

    @Query("SELECT s FROM ShopTypeEntity s WHERE s.deletedAt IS NULL " +
           "AND (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ShopTypeEntity> findActiveWithSearch(String search, Pageable pageable);

    @Query("SELECT s FROM ShopTypeEntity s WHERE s.deletedAt IS NOT NULL " +
           "AND (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ShopTypeEntity> findDeletedWithSearch(String search, Pageable pageable);
}
