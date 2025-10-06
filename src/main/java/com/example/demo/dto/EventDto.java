package com.example.demo.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class EventDto {

	@NotBlank(message = "Title is required")
	@Size(max = 100, message = "Title must not exceed 100 characters")
	private String title;

	@NotBlank(message = "Description is required")
	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	@NotNull(message = "Date is required")
	@Future(message = "Event date must be in the future")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime date;

	@NotBlank(message = "Location is required")
	@Size(max = 200, message = "Location must not exceed 200 characters")
	private String location;

	@NotNull(message = "Maximum participants is required")
	@Min(value = 1, message = "Maximum participants must be at least 1")
	@Max(value = 1000, message = "Maximum participants cannot exceed 1000")
	private Integer maxParticipants;

	@NotNull(message = "Category is required")
	private Long categoryId;

	// Constructors, getters and setters
	public EventDto() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
