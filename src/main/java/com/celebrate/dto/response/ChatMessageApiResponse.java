package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageApiResponse {
    private Boolean success;
    private String message;
    private ChatMessageOutputResponse data;
}
