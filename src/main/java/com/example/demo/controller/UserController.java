package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/profile")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String profile(Model model) {
		User currentUser = userService.getCurrentUser();
		model.addAttribute("user", currentUser);
		return "profile";
	}

	@PostMapping("/update")
	public String updateProfile(@RequestParam String username,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
			RedirectAttributes redirectAttributes) {
		try {
			User currentUser = userService.getCurrentUser();
			userService.updateProfile(currentUser, username);

			if (profileImage != null && !profileImage.isEmpty()) {
				userService.updateProfileImage(currentUser, profileImage);
			}

			redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/profile";
	}
}
