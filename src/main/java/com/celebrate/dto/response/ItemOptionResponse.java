package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOptionResponse {
    private String id;
    private String title;
    private String description;
    private Double price;

    public String get_id() { return id; }
}
