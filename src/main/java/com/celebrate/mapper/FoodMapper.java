package com.celebrate.mapper;

import com.celebrate.dto.response.FoodResponse;
import com.celebrate.entity.FoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VariationMapper.class})
public interface FoodMapper {

    @Mapping(source = "subCategory.id", target = "subCategory")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "images", target = "images") // Explicitly map the list
    FoodResponse toResponse(FoodEntity entity);

    List<FoodResponse> toResponseList(List<FoodEntity> entities);
}
