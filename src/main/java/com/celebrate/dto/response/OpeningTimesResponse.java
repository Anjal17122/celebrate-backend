package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningTimesResponse {
    private String day;
    private List<TimingsResponse> times;
}
