package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByCustomerCustomerId(Long customerId);
}
