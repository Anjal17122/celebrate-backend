package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageOutputResponse {
    private String id;
    private String message;
    private ChatUserOutputResponse user;
    private String createdAt;
    // used for subscription filtering only (not in GraphQL schema)
    private String orderId;
    public String get_id() { return id; }
}
