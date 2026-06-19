package com.celebrate.mapper;

import com.celebrate.dto.response.ShopTypeResponse;
import com.celebrate.entity.ShopTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShopTypeMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "deletedAt", target = "deletedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ShopTypeResponse toResponse(ShopTypeEntity entity);

    List<ShopTypeResponse> toResponseList(List<ShopTypeEntity> entities);
}
