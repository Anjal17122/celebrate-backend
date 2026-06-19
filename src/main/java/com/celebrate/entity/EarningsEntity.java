package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "earnings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarningsEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "order_id", length = 36)
    private String orderId;

    @Column(name = "order_type", length = 50)
    private String orderType;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    // Platform earnings
    @Column(name = "marketplace_commission")
    private Double marketplaceCommission;

    @Column(name = "delivery_commission")
    private Double deliveryCommission;

    @Column(name = "platform_tax")
    private Double tax;

    @Column(name = "platform_fee")
    private Double platformFee;

    @Column(name = "platform_total_earnings")
    private Double platformTotalEarnings;

    // Rider earnings
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private RiderEntity rider;

    @Column(name = "rider_delivery_fee")
    private Double riderDeliveryFee;

    @Column(name = "rider_tip")
    private Double riderTip;

    @Column(name = "rider_total_earnings")
    private Double riderTotalEarnings;

    // Store earnings
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private RestaurantEntity store;

    @Column(name = "store_order_amount")
    private Double storeOrderAmount;

    @Column(name = "store_total_earnings")
    private Double storeTotalEarnings;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
