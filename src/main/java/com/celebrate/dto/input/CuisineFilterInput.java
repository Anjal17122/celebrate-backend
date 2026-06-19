package com.celebrate.dto.input;

import lombok.Data;

@Data
public class CuisineFilterInput {
    private String keyword;
    private Integer page;
    private Integer limit;
    private String shopType;
    private Boolean paginate;
}
