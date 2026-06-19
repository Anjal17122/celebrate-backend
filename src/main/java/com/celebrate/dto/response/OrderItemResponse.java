package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private String id;
    private String food;
    private String title;
    private String description;
    private String image;
    private Integer quantity;
    private ItemVariationResponse variation;
    private List<ItemAddonResponse> addons;
    private String specialInstructions;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;

    public String get_id() { return id; }
}
