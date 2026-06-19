package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String orderId;
    private RestaurantDetailResponse restaurant;
    private OrderAddressResponse deliveryAddress;
    private List<OrderItemResponse> items;
    private UserResponse user;
    private String paymentMethod;
    private Double paidAmount;
    private Double orderAmount;
    private Double discountAmount;
    private Boolean status;
    private String paymentStatus;
    private String orderStatus;
    private String reason;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private Double deliveryCharges;
    private Double tipping;
    private Double taxationAmount;
    private RiderResponse rider;
    private ReviewResponse review;
    private ZoneResponse zone;
    private String completionTime;
    private String orderDate;
    private String expectedTime;
    private String preparationTime;
    private Boolean isPickedUp;
    private String acceptedAt;
    private String pickedAt;
    private Integer selectedPrepTime;
    private String deliveredAt;
    private String cancelledAt;
    private String assignedAt;
    private Boolean isRinged;
    private Boolean isRiderRinged;
    private String instructions;
    private ChatResponse chat;

    public String get_id() { return id; }
}
