package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVariationResponse {
    private String id;
    private String title;
    private Double price;
    private Double discounted;

    public String get_id() { return id; }
}
