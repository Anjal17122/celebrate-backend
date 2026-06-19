package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerAuthDataResponse {
    private String userId;
    private String token;
    private Integer tokenExpiration;
    private String email;
    private String userType;
    private String userTypeId;
    private List<RestaurantResponse> restaurants;
    private List<String> permissions;
    private String pushToken;
    private String image;
    private String name;
}
