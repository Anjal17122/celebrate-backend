package com.celebrate.mapper;

import com.celebrate.dto.response.VariationResponse;
import com.celebrate.entity.VariationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariationMapper {

    @Mapping(source = "addonIds", target = "addons")
    VariationResponse toResponse(VariationEntity entity);

    List<VariationResponse> toResponseList(List<VariationEntity> entities);
}
