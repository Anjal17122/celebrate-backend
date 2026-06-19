package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private OrderResponse order;
    private RestaurantResponse restaurant;
    private Integer rating;
    private String description;
    private String comments;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;

    public String get_id() { return id; }
}
