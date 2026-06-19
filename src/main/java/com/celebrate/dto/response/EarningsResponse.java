package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarningsResponse {
    private String id;
    private String orderId;
    private String orderType;
    private String paymentMethod;
    private PlatformEarningsResponse platformEarnings;
    private RiderEarningsResponse riderEarnings;
    private StoreEarningsResponse storeEarnings;
    private String createdAt;
    private String updatedAt;

    public String get_id() { return id; }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatformEarningsResponse {
        private Double marketplaceCommission;
        private Double deliveryCommission;
        private Double tax;
        private Double platformFee;
        private Double totalEarnings;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiderEarningsResponse {
        private StackholderDetails riderId;
        private Double deliveryFee;
        private Double tip;
        private Double totalEarnings;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreEarningsResponse {
        private StackholderDetails storeId;
        private Double orderAmount;
        private Double totalEarnings;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StackholderDetails {
        private String id;
        private String name;
        private String username;

        public String get_id() { return id; }
    }
}
