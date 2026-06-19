package com.celebrate.dto.input;

import lombok.Data;

@Data
public class PaginationInput {
    private Integer pageSize;
    private Integer pageNo;
}
