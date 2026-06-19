package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingsResponse {
    private List<String> startTime;
    private List<String> endTime;
}
