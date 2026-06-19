package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String id;
    private String senderType;
    private String content;
    private Boolean isRead;
    private String ticket;
    private String createdAt;
    private String updatedAt;

    public String get_id() { return id; }
}
