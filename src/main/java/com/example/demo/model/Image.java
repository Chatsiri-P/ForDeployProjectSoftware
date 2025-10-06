package com.example.demo.model;

import java.sql.Blob;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {

	@Column(nullable = false)
	private String path;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ImageType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	public enum ImageType {
		PROFILE, EVENT
	}

	public Image() {
	}

	public Image(String path, ImageType type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ImageType getType() {
		return type;
	}

	public void setType(ImageType type) {
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