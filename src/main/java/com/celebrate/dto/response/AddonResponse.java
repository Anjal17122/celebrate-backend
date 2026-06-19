package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddonResponse {
    private String id;
    private List<String> options;
    private String title;
    private String description;
    private Integer quantityMinimum;
    private Integer quantityMaximum;
    private Boolean isOutOfStock;

    public String get_id() { return id; }
}
