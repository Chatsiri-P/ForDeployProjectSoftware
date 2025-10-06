package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class CommentDto {

	@NotBlank(message = "Comment text is required")
	@Size(max = 500, message = "Comment must not exceed 500 characters")
	private String text;

	private Long eventId;

	// Constructors, getters and setters
	public CommentDto() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}