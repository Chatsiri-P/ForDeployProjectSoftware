package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;

public interface NotificationStrategy {
	void deliver(User user, Notification notification);
	String getStrategyName();
}
