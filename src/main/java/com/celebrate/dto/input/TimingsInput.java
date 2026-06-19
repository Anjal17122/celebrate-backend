package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class TimingsInput {
    private String day;
    private List<TimesInput> times;
}
