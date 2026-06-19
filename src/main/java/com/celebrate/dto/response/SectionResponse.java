package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    private String id;
    private String name;
    private Boolean enabled;
    private List<SectionRestaurantResponse> restaurants;

    public String get_id() { return id; }
}
