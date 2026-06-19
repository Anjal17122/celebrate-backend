package com.celebrate.dto.input;

import lombok.Data;

@Data
public class FiltersInput {
    private String status;
    private String search;
    private Integer page;
    private Integer limit;
}
