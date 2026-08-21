package com.celebrate.mapper;

import com.celebrate.dto.response.FoodResponse;
import com.celebrate.entity.FoodEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VariationMapper.class, ImageUrlHelper.class})
public interface FoodMapper {

    @Mapping(source = "subCategory.id", target = "subCategory")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "image", target = "image", qualifiedByName = "resolveImageUrl")
    @Mapping(source = "images", target = "images", qualifiedByName = "resolveImageUrls")
    FoodResponse toResponse(FoodEntity entity);

    List<FoodResponse> toResponseList(List<FoodEntity> entities);
}
