package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    private String category;
    private String orderId;
    private String otherDetails;
    private String createdAt;
    private String updatedAt;
    private UserResponse user;
    private String userType;

    public String get_id() { return id; }
}
