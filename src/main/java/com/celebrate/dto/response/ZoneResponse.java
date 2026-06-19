package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {
    private String id;
    private String title;
    private Double tax;
    private PolygonResponse location;
    private String description;
    private Boolean isActive;

    public String get_id() { return id; }
}
