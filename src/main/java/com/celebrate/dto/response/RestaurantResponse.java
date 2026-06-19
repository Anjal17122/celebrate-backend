package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private String id;
    private String uniqueRestaurantId;
    private Integer orderId;
    private String orderPrefix;
    private String name;
    private String image;
    private String logo;
    private String address;
    private PointResponse location;
    private List<CategoryResponse> categories;
    private List<OptionResponse> options;
    private List<AddonResponse> addons;
    private ReviewDataResponse reviewData;
    private ZoneResponse zone;
    private String username;
    private String password;
    private Integer minimumOrder;
    private List<String> sections;
    private Double rating;
    private Boolean isActive;
    private Boolean isAvailable;
    private List<OpeningTimesResponse> openingTimes;
    private String slug;
    private Boolean stripeDetailsSubmitted;
    private Double commissionRate;
    private OwnerSimpleResponse owner;
    private Integer deliveryTime;
    private DeliveryInfoResponse deliveryInfo;
    private Double tax;
    private String notificationToken;
    private Boolean enableNotification;
    private String shopType;
    private List<String> cuisines;
    private List<String> keywords;
    private List<String> tags;
    private Integer reviewCount;
    private Double reviewAverage;
    private String restaurantUrl;
    private String phone;
    private BussinessDetailsResponse bussinessDetails;
    private Double currentWalletAmount;
    private Double totalWalletAmount;
    private Double withdrawnWalletAmount;
    private String city;
    private String postCode;

    public String get_id() { return id; }
}
