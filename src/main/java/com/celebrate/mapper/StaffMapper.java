package com.celebrate.mapper;

import com.celebrate.dto.response.StaffResponse;
import com.celebrate.entity.StaffEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    StaffResponse toResponse(StaffEntity entity);

    List<StaffResponse> toResponseList(List<StaffEntity> entities);
}
