package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDataResponse {
    private List<ReviewResponse> reviews;
    private Double ratings;
    private Integer total;
}
