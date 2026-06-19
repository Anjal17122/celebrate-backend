package com.celebrate.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOperationResponse {
    private Boolean success;
    private String message;
    private RestaurantResponse data;
}
