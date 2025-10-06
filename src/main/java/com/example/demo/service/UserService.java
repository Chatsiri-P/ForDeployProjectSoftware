package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final ImageService imageService;

	@Autowired
	public UserService(UserRepository userRepository, ImageService imageService) {
		this.userRepository = userRepository;
		this.imageService = imageService;
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Current user not found"));
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public void updateProfileImage(User user, MultipartFile imageFile) {
		if (imageFile != null && !imageFile.isEmpty()) {
			var image = imageService.saveProfileImage(imageFile, user);
			user.setProfileImagePath(image.getPath());
			userRepository.save(user);
		}
	}

	public void updateProfile(User user, String username) {
		if (username != null && !username.trim().isEmpty()) {
			user.setUsername(username.trim());
			userRepository.save(user);
		}
	}
}