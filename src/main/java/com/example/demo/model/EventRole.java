package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "event_roles")
public class EventRole extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "eventRole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<EventMember> eventMembers = new HashSet<>();

	public EventRole() {
	}

	public EventRole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<EventMember> getEventMembers() {
		return eventMembers;
	}

	public void setEventMembers(Set<EventMember> eventMembers) {
		this.eventMembers = eventMembers;
	}
}
