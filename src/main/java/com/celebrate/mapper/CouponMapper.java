package com.celebrate.mapper;

import com.celebrate.dto.response.CouponResponse;
import com.celebrate.entity.CouponEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(source = "startDate", target = "startDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "endDate", target = "endDate", dateFormat = "yyyy-MM-dd")
    CouponResponse toResponse(CouponEntity entity);

    List<CouponResponse> toResponseList(List<CouponEntity> entities);
}
