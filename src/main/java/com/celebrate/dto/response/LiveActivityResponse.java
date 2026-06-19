package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveActivityResponse {
    private Boolean success;
    private String message;
}
