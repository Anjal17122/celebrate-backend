package com.celebrate.mapper;

import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RiderMapper.class, ZoneMapper.class, RestaurantMapper.class})
public interface OrderMapper {

    @Mapping(source = ".", target = "deliveryAddress", qualifiedByName = "toOrderAddress")
    @Mapping(source = ".", target = "chat", qualifiedByName = "toChat")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "items", target = "items", qualifiedByName = "toItemList")
    OrderResponse toResponse(OrderEntity entity);

    List<OrderResponse> toResponseList(List<OrderEntity> entities);

    @Named("toOrderAddress")
    default OrderAddressResponse toOrderAddress(OrderEntity entity) {
        return OrderAddressResponse.builder()
                .deliveryAddress(entity.getDeliveryAddress())
                .details(entity.getDeliveryDetails())
                .label(entity.getDeliveryLabel())
                .location(entity.getDeliveryLat() != null
                        ? PointResponse.builder().coordinates(List.of(entity.getDeliveryLng(), entity.getDeliveryLat())).build()
                        : null)
                .build();
    }

    @Named("toChat")
    default ChatResponse toChat(OrderEntity entity) {
        if (entity.getChatMessage() == null) return null;
        return ChatResponse.builder()
                .message(entity.getChatMessage())
                .user(entity.getChatUser())
                .isActive(entity.getChatIsActive())
                .build();
    }

    @Named("toItemList")
    default List<OrderItemResponse> toItemList(List<OrderItemEntity> items) {
        if (items == null) return null;
        return items.stream().map(item -> {
            ItemVariationResponse var = null;
            if (item.getVariation() != null) {
                var = ItemVariationResponse.builder()
                        .id(item.getVariation().getId())
                        .title(item.getVariation().getTitle())
                        .price(item.getVariation().getPrice())
                        .discounted(item.getVariation().getDiscounted())
                        .build();
            }
            List<ItemAddonResponse> addons = null;
            if (item.getAddons() != null) {
                addons = item.getAddons().stream().map(a -> ItemAddonResponse.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .description(a.getDescription())
                        .quantityMinimum(a.getQuantityMinimum())
                        .quantityMaximum(a.getQuantityMaximum())
                        .options(a.getOptions() != null ? a.getOptions().stream().map(o ->
                                ItemOptionResponse.builder()
                                        .id(o.getId())
                                        .title(o.getTitle())
                                        .description(o.getDescription())
                                        .price(o.getPrice())
                                        .build()).collect(Collectors.toList()) : null)
                        .build()).collect(Collectors.toList());
            }
            return OrderItemResponse.builder()
                    .id(item.getId())
                    .food(item.getFoodId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .image(item.getImage())
                    .quantity(item.getQuantity())
                    .specialInstructions(item.getSpecialInstructions())
                    .cakeText(item.getCakeText())
                    .cakeImageUrl(item.getCakeImageUrl())
                    .isActive(item.getIsActive())
                    .variation(var)
                    .addons(addons)
                    .build();
        }).collect(Collectors.toList());
    }
}
