package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.NotificationResponse;
import com.chidicivok.civokbank.entities.Notification;

public class NotificationMapper {

    public static NotificationResponse toResponse(Notification notification) {

        NotificationResponse response = new NotificationResponse();

        response.setNotificationId(notification.getNotificationId());
        response.setNotificationType(notification.getNotificationType());
        response.setMessage(notification.getMessage());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}