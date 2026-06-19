package com.celebrate.dto.input;

import lombok.Data;

@Data
public class FetchShopTypeFilter {
    private String search;
    private Boolean isDeleted;
}
