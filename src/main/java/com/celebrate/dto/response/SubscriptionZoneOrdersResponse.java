package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionZoneOrdersResponse {
    private String zoneId;
    private OrderResponse order;
    private String origin;
}
