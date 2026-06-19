package com.celebrate.mapper;

import com.celebrate.dto.response.AddonResponse;
import com.celebrate.entity.AddonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddonMapper {

    @Mapping(source = "optionIds", target = "options")
    AddonResponse toResponse(AddonEntity entity);

    List<AddonResponse> toResponseList(List<AddonEntity> entities);
}
