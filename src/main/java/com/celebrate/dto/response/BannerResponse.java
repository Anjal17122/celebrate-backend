package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponse {
    private String id;
    private String title;
    private String description;
    private String file;
    private String action;
    private String screen;
    private String parameters;
    private String slug;
    private String shopType;

    public String get_id() { return id; }
}
