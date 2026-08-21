package com.celebrate.mapper;

import com.celebrate.dto.response.BannerResponse;
import com.celebrate.entity.BannerEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageUrlHelper.class})
public interface BannerMapper {

    @Mapping(source = "file", target = "file", qualifiedByName = "resolveImageUrl")
    BannerResponse toResponse(BannerEntity entity);

    List<BannerResponse> toResponseList(List<BannerEntity> entities);
}
