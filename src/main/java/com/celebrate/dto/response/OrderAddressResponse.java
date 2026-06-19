package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressResponse {
    private PointResponse location;
    private String deliveryAddress;
    private String details;
    private String label;
    private String id;
}
