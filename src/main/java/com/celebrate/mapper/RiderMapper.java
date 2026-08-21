package com.celebrate.mapper;

import com.celebrate.dto.response.*;
import com.celebrate.entity.DayScheduleEntity;
import com.celebrate.entity.RiderEntity;
import com.celebrate.entity.TimeSlotEntity;
import com.celebrate.utils.ImageUrlHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class, ImageUrlHelper.class})
public abstract class RiderMapper {

    @Autowired
    protected ImageUrlHelper imageUrlHelper;

    @Mapping(source = ".", target = "location", qualifiedByName = "toPoint")
    @Mapping(source = ".", target = "bussinessDetails", qualifiedByName = "toBussinessDetails")
    @Mapping(source = ".", target = "licenseDetails", qualifiedByName = "toLicenseDetails")
    @Mapping(source = ".", target = "vehicleDetails", qualifiedByName = "toVehicleDetails")
    @Mapping(source = "workSchedule", target = "workSchedule", qualifiedByName = "toScheduleList")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "image", target = "image", qualifiedByName = "resolveImageUrl")
    public abstract RiderResponse toResponse(RiderEntity entity);

    public abstract List<RiderResponse> toResponseList(List<RiderEntity> entities);

    @Named("toPoint")
    public PointResponse toPoint(RiderEntity entity) {
        if (entity.getLat() == null && entity.getLng() == null) return null;
        return PointResponse.builder()
                .coordinates(List.of(entity.getLng() != null ? entity.getLng() : "",
                                     entity.getLat() != null ? entity.getLat() : ""))
                .build();
    }

    @Named("toBussinessDetails")
    public BussinessDetailsResponse toBussinessDetails(RiderEntity entity) {
        if (entity.getBankName() == null) return null;
        return BussinessDetailsResponse.builder()
                .bankName(entity.getBankName())
                .accountName(entity.getAccountName())
                .accountCode(entity.getAccountCode())
                .accountNumber(entity.getAccountNumber())
                .bussinessRegNo(entity.getBussinessRegNo())
                .companyRegNo(entity.getCompanyRegNo())
                .taxRate(entity.getTaxRate())
                .build();
    }

    @Named("toLicenseDetails")
    public LicenseDetailsResponse toLicenseDetails(RiderEntity entity) {
        if (entity.getLicenseNumber() == null) return null;
        return LicenseDetailsResponse.builder()
                .number(entity.getLicenseNumber())
                .expiryDate(entity.getLicenseExpiryDate())
                .image(imageUrlHelper.resolve(entity.getLicenseImage()))
                .build();
    }

    @Named("toVehicleDetails")
    public VehicleDetailsResponse toVehicleDetails(RiderEntity entity) {
        if (entity.getVehicleNumber() == null) return null;
        return VehicleDetailsResponse.builder()
                .number(entity.getVehicleNumber())
                .image(imageUrlHelper.resolve(entity.getVehicleImage()))
                .build();
    }

    @Named("toScheduleList")
    public List<DayScheduleResponse> toScheduleList(List<DayScheduleEntity> schedules) {
        if (schedules == null) return null;
        return schedules.stream().map(s -> DayScheduleResponse.builder()
                .day(s.getDay())
                .enabled(s.getEnabled())
                .slots(toSlotList(s.getSlots()))
                .build()).collect(Collectors.toList());
    }

    public List<TimeSlotResponse> toSlotList(List<TimeSlotEntity> slots) {
        if (slots == null) return null;
        return slots.stream().map(sl -> TimeSlotResponse.builder()
                .startTime(sl.getStartTime())
                .endTime(sl.getEndTime())
                .build()).collect(Collectors.toList());
    }
}
