package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestResponse {
    private String id;
    private String requestId;
    private Double requestAmount;
    private String requestTime;
    private RiderResponse rider;
    private RestaurantResponse store;
    private String status;
    private String createdAt;

    public String get_id() { return id; }
}
