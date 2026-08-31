package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.responses.NotificationResponse;
import com.chidicivok.civokbank.services.interfaces.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/my-notifications")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        List<NotificationResponse> notifications = notificationService.getCustomerNotifications(authentication.getName());
        return ResponseEntity.ok(notifications);
    }
}