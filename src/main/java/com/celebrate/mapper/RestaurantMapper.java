package com.celebrate.mapper;

import com.celebrate.dto.response.*;
import com.celebrate.entity.OpeningTimeEntity;
import com.celebrate.entity.RestaurantEntity;
import com.celebrate.entity.TimingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, OptionMapper.class, AddonMapper.class, ZoneMapper.class})
public interface RestaurantMapper {

    @Mapping(source = ".", target = "location", qualifiedByName = "toPoint")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = ".", target = "bussinessDetails", qualifiedByName = "toBussinessDetails")
    @Mapping(source = ".", target = "deliveryInfo", qualifiedByName = "toDeliveryInfo")
    @Mapping(source = "shopType.name", target = "shopType")
    RestaurantResponse toResponse(RestaurantEntity entity);

    @Mapping(source = "times", target = "times")
    OpeningTimesResponse toOpeningTimesResponse(OpeningTimeEntity entity);

    @Mapping(target = "startTime", expression = "java(splitStringToList(entity.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(splitStringToList(entity.getEndTime()))")
    TimingsResponse toTimingsResponse(TimingEntity entity);

    // 3. Helper method for the String-to-List conversion
    default List<String> splitStringToList(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(value.split(","));
    }

    @Mapping(source = ".", target = "location", qualifiedByName = "toPoint")
    @Mapping(source = "shopType.name", target = "shopType")
    RestaurantDetailResponse toDetailResponse(RestaurantEntity entity);

    List<RestaurantResponse> toResponseList(List<RestaurantEntity> entities);

    @Named("toPoint")
    default PointResponse toPoint(RestaurantEntity entity) {
        if (entity.getLat() == null && entity.getLng() == null) return null;
        return PointResponse.builder()
                .coordinates(List.of(entity.getLng() != null ? entity.getLng() : "",
                                     entity.getLat() != null ? entity.getLat() : ""))
                .build();
    }

    @Named("toBussinessDetails")
    default BussinessDetailsResponse toBussinessDetails(RestaurantEntity entity) {
        if (entity.getBankName() == null) return null;
        return BussinessDetailsResponse.builder()
                .bankName(entity.getBankName())
                .accountName(entity.getAccountName())
                .accountCode(entity.getAccountCode())
                .taxRate(entity.getTaxRate())
                .build();
    }

    @Named("toDeliveryInfo")
    default DeliveryInfoResponse toDeliveryInfo(RestaurantEntity entity) {
        return DeliveryInfoResponse.builder()
                .minDeliveryFee(entity.getMinDeliveryFee())
                .deliveryDistance(entity.getDeliveryDistance())
                .deliveryFee(entity.getDeliveryFee())
                .build();
    }

    default OwnerSimpleResponse ownerToSimple(com.celebrate.entity.OwnerEntity owner) {
        if (owner == null) return null;
        return OwnerSimpleResponse.builder()
                .id(owner.getId())
                .email(owner.getEmail())
                .isActive(owner.getIsActive())
                .build();
    }
}
