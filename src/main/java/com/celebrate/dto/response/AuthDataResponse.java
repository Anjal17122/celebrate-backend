package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDataResponse {
    private String userId;
    private String token;
    private Integer tokenExpiration;
    private String name;
    private String phone;
    private Boolean phoneIsVerified;
    private String email;
    private Boolean emailIsVerified;
    private String picture;
    private Boolean isNewUser;
    private String userTypeId;
    private Boolean isActive;
}
