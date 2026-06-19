package com.celebrate.mapper;

import com.celebrate.dto.response.*;
import com.celebrate.entity.DayScheduleEntity;
import com.celebrate.entity.RiderEntity;
import com.celebrate.entity.TimeSlotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public interface RiderMapper {

    @Mapping(source = ".", target = "location", qualifiedByName = "toPoint")
    @Mapping(source = ".", target = "bussinessDetails", qualifiedByName = "toBussinessDetails")
    @Mapping(source = ".", target = "licenseDetails", qualifiedByName = "toLicenseDetails")
    @Mapping(source = ".", target = "vehicleDetails", qualifiedByName = "toVehicleDetails")
    @Mapping(source = "workSchedule", target = "workSchedule", qualifiedByName = "toScheduleList")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    RiderResponse toResponse(RiderEntity entity);

    List<RiderResponse> toResponseList(List<RiderEntity> entities);

    @Named("toPoint")
    default PointResponse toPoint(RiderEntity entity) {
        if (entity.getLat() == null && entity.getLng() == null) return null;
        return PointResponse.builder()
                .coordinates(List.of(entity.getLng() != null ? entity.getLng() : "",
                                     entity.getLat() != null ? entity.getLat() : ""))
                .build();
    }

    @Named("toBussinessDetails")
    default BussinessDetailsResponse toBussinessDetails(RiderEntity entity) {
        if (entity.getBankName() == null) return null;
        return BussinessDetailsResponse.builder()
                .bankName(entity.getBankName())
                .accountName(entity.getAccountName())
                .accountCode(entity.getAccountCode())
                .taxRate(entity.getTaxRate())
                .build();
    }

    @Named("toLicenseDetails")
    default LicenseDetailsResponse toLicenseDetails(RiderEntity entity) {
        if (entity.getLicenseNumber() == null) return null;
        return LicenseDetailsResponse.builder()
                .number(entity.getLicenseNumber())
                .expiryDate(entity.getLicenseExpiryDate())
                .image(entity.getLicenseImage())
                .build();
    }

    @Named("toVehicleDetails")
    default VehicleDetailsResponse toVehicleDetails(RiderEntity entity) {
        if (entity.getVehicleNumber() == null) return null;
        return VehicleDetailsResponse.builder()
                .number(entity.getVehicleNumber())
                .image(entity.getVehicleImage())
                .build();
    }

    @Named("toScheduleList")
    default List<DayScheduleResponse> toScheduleList(List<DayScheduleEntity> schedules) {
        if (schedules == null) return null;
        return schedules.stream().map(s -> DayScheduleResponse.builder()
                .day(s.getDay())
                .enabled(s.getEnabled())
                .slots(toSlotList(s.getSlots()))
                .build()).collect(Collectors.toList());
    }

    default List<TimeSlotResponse> toSlotList(List<TimeSlotEntity> slots) {
        if (slots == null) return null;
        return slots.stream().map(sl -> TimeSlotResponse.builder()
                .startTime(sl.getStartTime())
                .endTime(sl.getEndTime())
                .build()).collect(Collectors.toList());
    }
}
