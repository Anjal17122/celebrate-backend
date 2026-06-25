package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationResponse {
    private String id;
    private String title;
    private Double price;
    private Double discounted;
    private List<String> addons;
    private Boolean isOutOfStock;
    private Integer prepTime;

    public String get_id() { return id; }
}
