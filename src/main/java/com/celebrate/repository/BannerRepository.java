package com.celebrate.repository;

import com.celebrate.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, String> {

    List<BannerEntity> findByShopType(String shopType);

    java.util.Optional<BannerEntity> findByTitle(String title);
}
