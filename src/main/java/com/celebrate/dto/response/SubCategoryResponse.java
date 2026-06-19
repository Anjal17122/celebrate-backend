package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponse {
    private String id;
    private String title;
    private String parentCategoryId;

    public String get_id() { return id; }
}
