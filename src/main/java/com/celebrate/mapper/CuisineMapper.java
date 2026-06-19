package com.celebrate.mapper;

import com.celebrate.dto.response.CuisineResponse;
import com.celebrate.entity.CuisineEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CuisineMapper {

    CuisineResponse toResponse(CuisineEntity entity);

    List<CuisineResponse> toResponseList(List<CuisineEntity> entities);
}
