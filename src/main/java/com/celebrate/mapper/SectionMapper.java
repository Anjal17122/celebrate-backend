package com.celebrate.mapper;

import com.celebrate.dto.response.SectionResponse;
import com.celebrate.dto.response.SectionRestaurantResponse;
import com.celebrate.entity.RestaurantEntity;
import com.celebrate.entity.SectionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    SectionResponse toResponse(SectionEntity entity);
    List<SectionResponse> toResponseList(List<SectionEntity> entities);

    default SectionRestaurantResponse restaurantToSimple(RestaurantEntity entity) {
        if (entity == null) return null;
        return SectionRestaurantResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
