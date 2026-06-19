package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DemoCredentialsResponse {
    private String restaurantUsername;
    private String restaurantPassword;
    private String riderUsername;
    private String riderPassword;
}
