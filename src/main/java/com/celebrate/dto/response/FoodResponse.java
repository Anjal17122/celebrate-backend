package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private String id;
    private String title;
    private String description;
    private List<VariationResponse> variations;
    private String image;
    private List<String> images;
    private String subCategory;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private Boolean isOutOfStock;

    public String get_id() { return id; }
}
