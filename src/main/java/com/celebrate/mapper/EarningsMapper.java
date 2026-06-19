package com.celebrate.mapper;

import com.celebrate.dto.response.EarningsResponse;
import com.celebrate.entity.EarningsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EarningsMapper {

    @Mapping(source = ".", target = "platformEarnings", qualifiedByName = "toPlatform")
    @Mapping(source = ".", target = "riderEarnings", qualifiedByName = "toRiderEarnings")
    @Mapping(source = ".", target = "storeEarnings", qualifiedByName = "toStoreEarnings")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    EarningsResponse toResponse(EarningsEntity entity);

    List<EarningsResponse> toResponseList(List<EarningsEntity> entities);

    @Named("toPlatform")
    default EarningsResponse.PlatformEarningsResponse toPlatform(EarningsEntity e) {
        return EarningsResponse.PlatformEarningsResponse.builder()
                .marketplaceCommission(e.getMarketplaceCommission())
                .deliveryCommission(e.getDeliveryCommission())
                .tax(e.getTax())
                .platformFee(e.getPlatformFee())
                .totalEarnings(e.getPlatformTotalEarnings())
                .build();
    }

    @Named("toRiderEarnings")
    default EarningsResponse.RiderEarningsResponse toRiderEarnings(EarningsEntity e) {
        if (e.getRider() == null) return null;
        return EarningsResponse.RiderEarningsResponse.builder()
                .riderId(EarningsResponse.StackholderDetails.builder()
                        .id(e.getRider().getId())
                        .name(e.getRider().getName())
                        .username(e.getRider().getUsername())
                        .build())
                .deliveryFee(e.getRiderDeliveryFee())
                .tip(e.getRiderTip())
                .totalEarnings(e.getRiderTotalEarnings())
                .build();
    }

    @Named("toStoreEarnings")
    default EarningsResponse.StoreEarningsResponse toStoreEarnings(EarningsEntity e) {
        if (e.getStore() == null) return null;
        return EarningsResponse.StoreEarningsResponse.builder()
                .storeId(EarningsResponse.StackholderDetails.builder()
                        .id(e.getStore().getId())
                        .name(e.getStore().getName())
                        .username(e.getStore().getUsername())
                        .build())
                .orderAmount(e.getStoreOrderAmount())
                .totalEarnings(e.getStoreTotalEarnings())
                .build();
    }
}
