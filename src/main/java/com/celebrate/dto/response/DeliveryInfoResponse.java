package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoResponse {
    private Double minDeliveryFee;
    private Double deliveryDistance;
    private Double deliveryFee;
}
