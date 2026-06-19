package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {
    private String id;
    private String name;
    private String tag;
    private List<RestaurantResponse> restaurants;

    public String get_id() { return id; }
}
