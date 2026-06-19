package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String title;
    private String body;
    private String updatedAt;
    private String createdAt;

    public String get_id() { return id; }
}
