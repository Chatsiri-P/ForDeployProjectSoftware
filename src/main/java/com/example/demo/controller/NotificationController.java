package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.service.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final UserService userService;

	@Autowired
	public NotificationController(NotificationService notificationService, UserService userService) {
		this.notificationService = notificationService;
		this.userService = userService;
	}

	@GetMapping
	public String notifications(Model model) {
		User currentUser = userService.getCurrentUser();
		List<Notification> notifications = notificationService.getNotificationsForUser(currentUser);
	    Long unreadCount = notificationService.getUnreadNotificationCount(currentUser);

	    // Debug output
	    System.out.println("Current User: " + currentUser.getUsername());
	    System.out.println("Notifications: " + notifications);
	    System.out.println("Unread Count: " + unreadCount);

	    model.addAttribute("notifications", notifications);
	    model.addAttribute("unreadCount", unreadCount);
		return "noti";
	}

	@PostMapping("/{id}/mark-read")
	public String markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return "redirect:/notifications";
	}
}