package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;

@Component
public class InAppNotificationStrategy implements NotificationStrategy {

    private final NotificationRepository notificationRepository;

    @Autowired
    public InAppNotificationStrategy(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void deliver(User user, Notification notification) {
        notification.setUser(user);
        notificationRepository.save(notification);
    }

    @Override
    public String getStrategyName() {
        return "IN_APP";
    }
}