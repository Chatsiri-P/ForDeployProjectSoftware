package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Event;
import com.example.demo.model.Notification;
import com.example.demo.model.User;

@Service
public class NotificationFactory {

    public Notification createEventJoinNotification(User joiner, Event event) {
        String message = String.format("%s has joined your event '%s'", 
                joiner.getUsername(), event.getTitle());
        return new Notification(message, Notification.NotificationType.EVENT_JOIN, null, event);
    }

    public Notification createEventReminderNotification(Event event) {
        String message = String.format("Reminder: Event '%s' is starting soon at %s", 
                event.getTitle(), event.getLocation());
        return new Notification(message, Notification.NotificationType.EVENT_REMINDER, null, event);
    }

    public Notification createEventUpdateNotification(Event event) {
        String message = String.format("Event '%s' has been updated", event.getTitle());
        return new Notification(message, Notification.NotificationType.EVENT_UPDATE, null, event);
    }

}
