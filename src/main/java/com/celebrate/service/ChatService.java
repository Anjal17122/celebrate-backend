package com.celebrate.service;

import com.celebrate.dto.response.ChatMessageOutputResponse;
import com.celebrate.dto.response.ChatUserOutputResponse;
import com.celebrate.entity.OrderEntity;
import com.celebrate.exception.NotFoundException;
import com.celebrate.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OrderRepository orderRepository;

    public List<ChatMessageOutputResponse> getChat(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));

        // Chat messages are stored on the order entity if present
        if (order.getChatMessage() == null || order.getChatMessage().isBlank()) {
            return new ArrayList<>();
        }

        // Return the single chat message stored on the order
        ChatUserOutputResponse user = ChatUserOutputResponse.builder()
                .id(order.getUser() != null ? order.getUser().getId() : "")
                .name(order.getUser() != null ? order.getUser().getName() : "")
                .build();

        ChatMessageOutputResponse msg = ChatMessageOutputResponse.builder()
                .id(order.getId())
                .message(order.getChatMessage())
                .user(user)
                .createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "")
                .build();

        return List.of(msg);
    }
}
