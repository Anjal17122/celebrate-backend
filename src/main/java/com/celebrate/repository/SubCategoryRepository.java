package com.celebrate.repository;

import com.celebrate.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, String> {

    List<SubCategoryEntity> findByParentCategoryId(String parentCategoryId);

    void deleteByIdIn(List<String> ids);
}
