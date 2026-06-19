package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebNotificationResponse {
    private String id;
    private String body;
    private String navigateTo;
    private Boolean read;
    private String createdAt;

    public String get_id() { return id; }
}
