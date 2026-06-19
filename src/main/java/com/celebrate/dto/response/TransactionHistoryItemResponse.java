package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TransactionHistoryItemResponse {
    private String id;
    private String amountCurrency;
    private String status;
    private String transactionId;
    private String userType;
    private String userId;
    private Double amountTransferred;
    private BankDetailsResponse toBank;
    private RiderResponse rider;
    private RestaurantResponse store;
    private String createdAt;
    public String get_id() { return id; }
}
