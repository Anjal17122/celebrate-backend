package com.celebrate.mapper;

import com.celebrate.dto.response.OfferResponse;
import com.celebrate.entity.OfferEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RestaurantMapper.class})
public interface OfferMapper {
    OfferResponse toResponse(OfferEntity entity);
    List<OfferResponse> toResponseList(List<OfferEntity> entities);
}
