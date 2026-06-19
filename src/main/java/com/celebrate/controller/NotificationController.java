package com.celebrate.controller;

import com.celebrate.dto.response.NotificationResponse;
import com.celebrate.dto.response.WebNotificationResponse;
import com.celebrate.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @QueryMapping
    public List<NotificationResponse> notifications() {
        return notificationService.getAllNotifications();
    }

    @QueryMapping
    public List<WebNotificationResponse> webNotifications() {
        return notificationService.getWebNotifications();
    }

    @MutationMapping
    public List<WebNotificationResponse> markWebNotificationsAsRead() {
        return notificationService.markWebNotificationsAsRead();
    }

    @MutationMapping
    public String sendNotificationUser(@Argument String notificationTitle, @Argument String notificationBody) {
        return notificationService.sendNotificationUser(notificationTitle, notificationBody);
    }
}
