package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationDto {

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotBlank(message = "Confirm Password is required")
	private String confirmPassword;

	// Constructors, getters and setters
	public UserRegistrationDto() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
