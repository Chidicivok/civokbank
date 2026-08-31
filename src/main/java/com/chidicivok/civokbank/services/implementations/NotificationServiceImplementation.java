package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.NotificationResponse;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.entities.Notification;
import com.chidicivok.civokbank.enums.NotificationType;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.mappers.NotificationMapper;
import com.chidicivok.civokbank.repositories.CustomerRepository;
import com.chidicivok.civokbank.repositories.NotificationRepository;
import com.chidicivok.civokbank.services.interfaces.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;

    public NotificationServiceImplementation(NotificationRepository notificationRepository, CustomerRepository customerRepository) {
        this.notificationRepository = notificationRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public NotificationResponse createNotification(Customer customer, NotificationType notificationType, String message) {

        Notification notification = new Notification();

        notification.setCustomer(customer);
        notification.setNotificationType(notificationType);
        notification.setMessage(message);

        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.toResponse(savedNotification);
    }


    @Override
    public List<NotificationResponse> getCustomerNotifications(String customerEmail) {

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        return notificationRepository.findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }
}