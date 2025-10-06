package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Event;
import com.example.demo.service.EventService;

public class EventViewDto {
	private Event event;
	private boolean bookmarked;
	private boolean canJoin;
	private boolean pastEvent;
	private boolean isMember;
	private boolean isCreator;

	// constructor
	public EventViewDto(Event event, boolean bookmarked, boolean canJoin, boolean pastEvent, boolean isMember,
			boolean isCreator) {
		this.event = event;
		this.bookmarked = bookmarked;
		this.canJoin = canJoin;
		this.pastEvent = pastEvent;
		this.isMember = isMember;
		this.isCreator = isCreator;
	}

	// getters
	public Event getEvent() {
		return event;
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public boolean isCanJoin() {
		return canJoin;
	}

	public boolean isPastEvent() {
		return pastEvent;
	}

	public boolean isMember() {
		return isMember;
	}

	public boolean isCreator() {
		return isCreator;
	}
	
}
