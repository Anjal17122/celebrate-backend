package com.celebrate.service;

import com.celebrate.dto.response.NotificationResponse;
import com.celebrate.dto.response.WebNotificationResponse;
import com.celebrate.entity.NotificationEntity;
import com.celebrate.entity.WebNotificationEntity;
import com.celebrate.exception.*;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final WebNotificationRepository webNotificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(n -> NotificationResponse.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .body(n.getBody())
                        .createdAt(n.getCreatedAt() != null ? n.getCreatedAt().toString() : null)
                        .updatedAt(n.getUpdatedAt() != null ? n.getUpdatedAt().toString() : null)
                        .build())
                .toList();
    }

    public List<WebNotificationResponse> getWebNotifications() {
        String userId = SecurityUtil.getCurrentUserId();
        return webNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(n -> WebNotificationResponse.builder()
                        .id(n.getId())
                        .body(n.getBody())
                        .navigateTo(n.getNavigateTo())
                        .read(n.getRead())
                        .createdAt(n.getCreatedAt() != null ? n.getCreatedAt().toString() : null)
                        .build())
                .toList();
    }

    @Transactional
    public List<WebNotificationResponse> markWebNotificationsAsRead() {
        String userId = SecurityUtil.getCurrentUserId();
        webNotificationRepository.markAllAsReadByUserId(userId);
        return getWebNotifications();
    }

    public String sendNotificationUser(String notificationTitle, String notificationBody) {
        SecurityUtil.requireRole("ADMIN");
        // TODO: integrate with Firebase FCM or similar push notification service
        NotificationEntity notification = NotificationEntity.builder()
                .title(notificationTitle)
                .body(notificationBody)
                .build();
        notificationRepository.save(notification);
        return "Notification queued for all users.";
    }
}
