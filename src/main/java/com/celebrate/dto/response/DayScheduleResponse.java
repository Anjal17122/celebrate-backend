package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayScheduleResponse {
    private String day;
    private Boolean enabled;
    private List<TimeSlotResponse> slots;
}
