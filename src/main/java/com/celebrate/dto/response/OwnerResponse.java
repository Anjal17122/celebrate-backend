package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponse {
    private String id;
    private String uniqueId;
    private String email;
    private String name;
    private String image;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String plainPassword;
    private String userType;
    private Boolean isActive;
    private List<RestaurantResponse> restaurants;
    private String pushToken;

    public String get_id() { return id; }
}
