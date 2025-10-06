package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_members", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "event_id" }))
public class EventMember extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_role_id", nullable = false)
	private EventRole eventRole;

	public EventMember() {
	}

	public EventMember(User user, Event event, EventRole eventRole) {
		this.user = user;
		this.event = event;
		this.eventRole = eventRole;
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

	public EventRole getEventRole() {
		return eventRole;
	}

	public void setEventRole(EventRole eventRole) {
		this.eventRole = eventRole;
	}
}
