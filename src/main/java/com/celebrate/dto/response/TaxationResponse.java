package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationResponse {
    private String id;
    private Double taxationCharges;
    private Boolean enabled;

    public String get_id() { return id; }
}
