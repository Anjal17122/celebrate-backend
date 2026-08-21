package com.celebrate.mapper;

import com.celebrate.dto.response.CuisineResponse;
import com.celebrate.entity.CuisineEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageUrlHelper.class})
public interface CuisineMapper {

    @Mapping(source = "image", target = "image", qualifiedByName = "resolveImageUrl")
    CuisineResponse toResponse(CuisineEntity entity);

    List<CuisineResponse> toResponseList(List<CuisineEntity> entities);
}
