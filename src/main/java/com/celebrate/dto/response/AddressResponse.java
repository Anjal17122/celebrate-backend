package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String id;
    private PointResponse location;
    private String deliveryAddress;
    private String details;
    private String label;
    private Boolean selected;

    public String get_id() { return id; }
}
