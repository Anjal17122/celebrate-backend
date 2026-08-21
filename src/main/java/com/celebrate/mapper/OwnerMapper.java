package com.celebrate.mapper;

import com.celebrate.dto.response.OwnerResponse;
import com.celebrate.entity.OwnerEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RestaurantMapper.class, ImageUrlHelper.class})
public interface OwnerMapper {

    @Mapping(source = "image", target = "image", qualifiedByName = "resolveImageUrl")
    OwnerResponse toResponse(OwnerEntity entity);

    List<OwnerResponse> toResponseList(List<OwnerEntity> entities);
}
