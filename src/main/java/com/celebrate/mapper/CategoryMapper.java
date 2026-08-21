package com.celebrate.mapper;

import com.celebrate.dto.response.CategoryResponse;
import com.celebrate.entity.CategoryEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FoodMapper.class, ImageUrlHelper.class})
public interface CategoryMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "image", target = "image", qualifiedByName = "resolveImageUrl")
    CategoryResponse toResponse(CategoryEntity entity);

    List<CategoryResponse> toResponseList(List<CategoryEntity> entities);
}
