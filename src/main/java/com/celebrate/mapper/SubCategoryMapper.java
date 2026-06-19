package com.celebrate.mapper;

import com.celebrate.dto.response.SubCategoryResponse;
import com.celebrate.entity.SubCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    SubCategoryResponse toResponse(SubCategoryEntity entity);

    List<SubCategoryResponse> toResponseList(List<SubCategoryEntity> entities);
}
