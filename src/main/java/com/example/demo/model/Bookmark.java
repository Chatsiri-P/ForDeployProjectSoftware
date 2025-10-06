package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bookmarks", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "event_id" }))
public class Bookmark extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	public Bookmark() {
	}

	public Bookmark(User user, Event event) {
		this.user = user;
		this.event = event;
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
