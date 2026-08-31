package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.NotificationResponse;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(Customer customer, NotificationType notificationType, String message);

    List<NotificationResponse> getCustomerNotifications(String customerEmail);
}