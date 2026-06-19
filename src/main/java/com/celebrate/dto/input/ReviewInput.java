package com.celebrate.dto.input;

import lombok.Data;

@Data
public class ReviewInput {
    private String order;
    private Integer rating;
    private String comments;
    private String description;
}
