package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionOrdersResponse {
    private String restaurantId;
    private String userId;
    private OrderResponse order;
    private String origin;
    // used for internal filtering only (not in GraphQL schema)
    private String riderId;
}
