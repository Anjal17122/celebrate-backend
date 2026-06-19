package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private String id;
    private String title;
    private Double discount;
    private Boolean enabled;
    private String startDate;
    private String endDate;
    private Boolean lifeTimeActive;

    public String get_id() { return id; }
}
