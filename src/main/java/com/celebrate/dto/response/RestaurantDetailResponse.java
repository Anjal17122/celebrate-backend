package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailResponse {
    private String id;
    private String name;
    private String image;
    private String address;
    private PointResponse location;
    private String slug;
    private List<String> keywords;
    private List<String> tags;
    private Integer reviewCount;
    private Double reviewAverage;
    private String shopType;

    public String get_id() { return id; }
}
