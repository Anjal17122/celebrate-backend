package com.celebrate.mapper;

import com.celebrate.dto.response.ZoneResponse;
import com.celebrate.entity.ZoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "locationCoordinates", target = "location.coordinates")
    ZoneResponse toResponse(ZoneEntity entity);
}
