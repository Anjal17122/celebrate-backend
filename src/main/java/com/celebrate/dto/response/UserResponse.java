package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Boolean isActive;
    private String userType;
    private Boolean phoneIsVerified;
    private Boolean emailIsVerified;
    private String password;
    private String status;
    private String statusReason;
    private String lastLogin;
    private Boolean isOrderNotification;
    private Boolean isOfferNotification;
    private String createdAt;
    private String updatedAt;
    private List<AddressResponse> addresses;
    private String notificationToken;
    private List<String> favourite;
    private String notes;

    public String get_id() { return id; }
}
