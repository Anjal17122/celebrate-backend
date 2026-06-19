package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RestaurantEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "unique_restaurant_id", length = 100)
    private String uniqueRestaurantId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_prefix", length = 50)
    private String orderPrefix;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String image;

    @Column(length = 500)
    private String logo;

    @Column(length = 500)
    private String address;

    @Column(name = "lat", length = 100)
    private String lat;

    @Column(name = "lng", length = 100)
    private String lng;

    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(name = "minimum_order")
    private Integer minimumOrder;

    private Double rating;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_available")
    private Boolean isAvailable;

    private String slug;

    @Column(name = "stripe_details_submitted")
    private Boolean stripeDetailsSubmitted;

    @Column(name = "commission_rate")
    private Double commissionRate;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    private Double tax;

    @Column(name = "notification_token", columnDefinition = "TEXT")
    private String notificationToken;

    @Column(name = "enable_notification")
    private Boolean enableNotification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_type")
    @ToString.Exclude
    private ShopTypeEntity shopType;

    @Column(name = "restaurant_url", length = 500)
    private String restaurantUrl;

    @Column(length = 50)
    private String phone;

    @Column(name = "current_wallet_amount")
    private Double currentWalletAmount;

    @Column(name = "total_wallet_amount")
    private Double totalWalletAmount;

    @Column(name = "withdrawn_wallet_amount")
    private Double withdrawnWalletAmount;

    @Column(length = 100)
    private String city;

    @Column(name = "post_code", length = 20)
    private String postCode;

    @Column(name = "delivery_bounds", columnDefinition = "TEXT")
    private String deliveryBounds;

    @Column(name = "delivery_bounds_type", length = 50)
    private String deliveryBoundsType;

    @Column(name = "circle_bounds_radius")
    private Double circleBoundsRadius;

    @Column(name = "min_delivery_fee")
    private Double minDeliveryFee;

    @Column(name = "delivery_distance")
    private Double deliveryDistance;

    @Column(name = "delivery_fee")
    private Double deliveryFee;

    // BussinessDetails embedded
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bussiness_reg_no")
    private String bussinessRegNo;

    @Column(name = "company_reg_no")
    private String companyRegNo;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "is_cloned")
    private Boolean isCloned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private OwnerEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    @ToString.Exclude
    private ZoneEntity zone;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CategoryEntity> categories;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OptionEntity> options;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AddonEntity> addons;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<OpeningTimeEntity> openingTimes;

    @ElementCollection
    @CollectionTable(name = "restaurant_cuisines", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "cuisine", length = 100)
    private List<String> cuisines;

    @ElementCollection
    @CollectionTable(name = "restaurant_keywords", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "keyword", length = 100)
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "restaurant_tags", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "tag", length = 100)
    private List<String> tags;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
