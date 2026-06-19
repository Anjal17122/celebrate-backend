package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class DayScheduleInput {
    private String day;
    private Boolean enabled;
    private List<TimeSlotInput> slots;
}
