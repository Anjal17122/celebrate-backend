package com.celebrate.mapper;

import com.celebrate.dto.response.BannerResponse;
import com.celebrate.entity.BannerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerMapper {

    BannerResponse toResponse(BannerEntity entity);

    List<BannerResponse> toResponseList(List<BannerEntity> entities);
}
