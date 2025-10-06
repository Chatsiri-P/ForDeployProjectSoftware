package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

	@NotBlank(message = "Category name is required")
	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "icon_class")
    private String iconClass;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Event> events = new HashSet<>();

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}
	
	public Category(String name, String description, String iconClass) {
        this.name = name;
        this.description = description;
        this.iconClass = iconClass;
    }

	public Category(String name, String description) {
		this.name = name;
		this.description =description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
