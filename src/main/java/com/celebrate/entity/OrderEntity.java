package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "order_amount")
    private Double orderAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    private Boolean status;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(name = "order_status", length = 50)
    private String orderStatus;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "delivery_charges")
    private Double deliveryCharges;

    @Column(nullable = false)
    private Double tipping;

    @Column(name = "taxation_amount", nullable = false)
    private Double taxationAmount;

    @Column(name = "completion_time", length = 100)
    private String completionTime;

    @Column(name = "order_date", nullable = false)
    private String orderDate;

    @Column(name = "expected_time", length = 100)
    private String expectedTime;

    @Column(name = "preparation_time", length = 100)
    private String preparationTime;

    @Column(name = "is_picked_up", nullable = false)
    private Boolean isPickedUp;

    @Column(name = "accepted_at")
    private String acceptedAt;

    @Column(name = "picked_at")
    private String pickedAt;

    @Column(name = "selected_prep_time")
    private Integer selectedPrepTime;

    @Column(name = "delivered_at")
    private String deliveredAt;

    @Column(name = "cancelled_at")
    private String cancelledAt;

    @Column(name = "assigned_at")
    private String assignedAt;

    @Column(name = "is_ringed", nullable = false)
    private Boolean isRinged;

    @Column(name = "is_rider_ringed", nullable = false)
    private Boolean isRiderRinged;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    // Delivery address (embedded)
    @Column(name = "delivery_lat", length = 100)
    private String deliveryLat;

    @Column(name = "delivery_lng", length = 100)
    private String deliveryLng;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "delivery_details", length = 500)
    private String deliveryDetails;

    @Column(name = "delivery_label", length = 100)
    private String deliveryLabel;

    // Chat (embedded)
    @Column(name = "chat_message", columnDefinition = "TEXT")
    private String chatMessage;

    @Column(name = "chat_user")
    private String chatUser;

    @Column(name = "chat_images", columnDefinition = "TEXT")
    private String chatImages;

    @Column(name = "chat_is_active")
    private Boolean chatIsActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    @ToString.Exclude
    private RiderEntity rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    @ToString.Exclude
    private ZoneEntity zone;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OrderItemEntity> items;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private ReviewEntity review;

    @Column(name = "coupon_code", length = 100)
    private String couponCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
