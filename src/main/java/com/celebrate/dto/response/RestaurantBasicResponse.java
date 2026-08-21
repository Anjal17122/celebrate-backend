package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBasicResponse {
    private String id;
    private String name;
    private String image;
    private String slug;

    public String get_id() { return id; }
}
