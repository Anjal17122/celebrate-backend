package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class TimesInput {
    private List<String> startTime;
    private List<String> endTime;
}
