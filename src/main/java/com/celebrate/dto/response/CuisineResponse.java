package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuisineResponse {
    private String id;
    private String name;
    private String description;
    private String image;
    private String shopType;

    public String get_id() { return id; }
}
