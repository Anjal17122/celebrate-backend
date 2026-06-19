package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {
    private String id;
    private String title;
    private String description;
    private Double price;
    private Boolean isOutOfStock;

    public String get_id() { return id; }
}
