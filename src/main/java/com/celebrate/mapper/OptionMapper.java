package com.celebrate.mapper;

import com.celebrate.dto.response.OptionResponse;
import com.celebrate.entity.OptionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    OptionResponse toResponse(OptionEntity entity);

    List<OptionResponse> toResponseList(List<OptionEntity> entities);
}
