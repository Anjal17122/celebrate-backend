package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CartFoodResponse {
    private String id;
    private String title;
    private String description;
    private CartVariationResponse variation;
    private String image;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    public String get_id() { return id; }
}
