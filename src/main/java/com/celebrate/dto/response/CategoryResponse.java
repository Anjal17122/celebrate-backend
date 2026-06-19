package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String title;
    private String image;
    private List<FoodResponse> foods;
    private String createdAt;
    private String updatedAt;

    public String get_id() { return id; }
}
