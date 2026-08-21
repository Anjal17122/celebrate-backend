package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodWithRestaurantResponse {
    private String id;
    private String title;
    private String description;
    private String image;
    private Boolean isActive;
    private Boolean isOutOfStock;
    private String subCategory;
    private String subCategoryTitle;
    private List<VariationResponse> variations;
    private RestaurantBasicResponse restaurant;

    public String get_id() { return id; }
}
