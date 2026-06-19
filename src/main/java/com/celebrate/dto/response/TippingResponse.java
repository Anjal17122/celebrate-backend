package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TippingResponse {
    private String id;
    private List<Double> tipVariations;
    private Boolean enabled;

    public String get_id() { return id; }
}
