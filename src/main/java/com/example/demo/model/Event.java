package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "events")
public class Event extends BaseEntity {

	@NotBlank(message = "Title is required")
	@Column(nullable = false)
	private String title;

	@NotBlank(message = "Description is required")
	@Column(columnDefinition = "TEXT")
	private String description;

	@NotNull(message = "Date is required")
	@Future(message = "Event date must be in the future")
	@Column(name = "event_date", nullable = false)
	private LocalDateTime date;

	@NotBlank(message = "Location is required")
	@Column(nullable = false)
	private String location;

	@Min(value = 1, message = "Maximum participants must be at least 1")
	@Column(name = "max_participants", nullable = false)
	private int maxParticipants;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = false)
	private User creator;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<EventMember> members = new HashSet<>();

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<>();

	// Single image path instead of multiple images
    @Column(name = "image_path")
    private String imagePath;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Bookmark> bookmarks = new HashSet<>();

	// Constructors, getters and setters
	public Event() {
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

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Set<EventMember> getMembers() {
		return members;
	}

	public void setMembers(Set<EventMember> members) {
		this.members = members;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Set<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(Set<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public Set<Bookmark> getBookmarksSet() {
		return bookmarks;
	}

	public void setBookmarksSet(Set<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public int getCurrentParticipants() {
		return members.size();
	}

	public boolean isFull() {
		return getCurrentParticipants() >= maxParticipants;
	}
}
