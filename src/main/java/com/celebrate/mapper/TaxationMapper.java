package com.celebrate.mapper;

import com.celebrate.dto.response.TaxationResponse;
import com.celebrate.entity.TaxationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaxationMapper {

    TaxationResponse toResponse(TaxationEntity entity);
}
