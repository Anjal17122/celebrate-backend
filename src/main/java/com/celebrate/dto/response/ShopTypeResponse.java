package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopTypeResponse {
    private String id;
    private String image;
    private String name;
    private String slug;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public String get_id() { return id; }
}
