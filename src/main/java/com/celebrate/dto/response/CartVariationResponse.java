package com.celebrate.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CartVariationResponse {
    private String id;
    private String title;
    private Double price;
    private Double discounted;
    private List<CartAddonResponse> addons;
    public String get_id() { return id; }
}
