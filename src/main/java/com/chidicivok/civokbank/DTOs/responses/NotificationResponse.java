package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {

    private Long notificationId;

    private NotificationType notificationType;

    private String message;

    private LocalDateTime createdAt;
}