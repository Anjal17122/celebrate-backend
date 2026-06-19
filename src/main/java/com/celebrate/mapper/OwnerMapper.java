package com.celebrate.mapper;

import com.celebrate.dto.response.OwnerResponse;
import com.celebrate.entity.OwnerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RestaurantMapper.class})
public interface OwnerMapper {

    OwnerResponse toResponse(OwnerEntity entity);

    List<OwnerResponse> toResponseList(List<OwnerEntity> entities);
}
