package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

	@Column(nullable = false)
	private String message;

	@Column(name = "is_read", nullable = false)
	private boolean read = false;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	public enum NotificationType {
		EVENT_JOIN, EVENT_REMINDER, EVENT_UPDATE
	}

	public Notification() {
	}

	public Notification(String message, NotificationType type, User user, Event event) {
		this.message = message;
		this.type = type;
		this.user = user;
		this.event = event;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
