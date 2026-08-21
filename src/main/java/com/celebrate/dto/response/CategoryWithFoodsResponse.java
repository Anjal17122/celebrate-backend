package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithFoodsResponse {
    private String id;
    private String title;
    private String image;
    private List<FoodWithRestaurantResponse> foods;

    public String get_id() { return id; }
}
