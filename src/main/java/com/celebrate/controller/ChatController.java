package com.celebrate.controller;

import com.celebrate.dto.response.ChatMessageOutputResponse;
import com.celebrate.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @QueryMapping
    public List<ChatMessageOutputResponse> chat(@Argument("order") String orderId) {
        return chatService.getChat(orderId);
    }
}
