package com.celebrate.mapper;

import com.celebrate.dto.response.AddressResponse;
import com.celebrate.dto.response.PointResponse;
import com.celebrate.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = ".", target = "location", qualifiedByName = "toPoint")
    AddressResponse toResponse(AddressEntity entity);

    List<AddressResponse> toResponseList(List<AddressEntity> entities);

    @Named("toPoint")
    default PointResponse toPoint(AddressEntity entity) {
        if (entity.getLat() == null && entity.getLng() == null) return null;
        return PointResponse.builder()
                .coordinates(List.of(entity.getLng() != null ? entity.getLng() : "",
                                     entity.getLat() != null ? entity.getLat() : ""))
                .build();
    }
}
