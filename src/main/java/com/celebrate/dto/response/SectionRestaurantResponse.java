package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionRestaurantResponse {
    private String id;
    private String name;

    public String get_id() { return id; }
}
