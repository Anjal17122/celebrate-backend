package com.celebrate.mapper;

import com.celebrate.dto.response.TippingResponse;
import com.celebrate.entity.TippingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TippingMapper {

    TippingResponse toResponse(TippingEntity entity);
}
