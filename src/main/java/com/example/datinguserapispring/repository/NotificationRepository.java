package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByIsSendFalseAndSendTimeLessThanEqual(LocalDateTime sendTime);

}
