package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.*;

@Component
public class EventJoinObserver implements EventObserver {

    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;

    @Autowired
    public EventJoinObserver(NotificationService notificationService, NotificationFactory notificationFactory) {
        this.notificationService = notificationService;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public void onUserJoinedEvent(User user, Event event) {
        // Create and send notification to event creator
        Notification notification = notificationFactory.createEventJoinNotification(user, event);
        notificationService.sendNotification(event.getCreator(), notification);
    }
}