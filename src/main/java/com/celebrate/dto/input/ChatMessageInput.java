package com.celebrate.dto.input;

import lombok.Data;

@Data
public class ChatMessageInput {
    private String message;
    private ChatUserInput user;
}
