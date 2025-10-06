package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null) {
			model.addAttribute("error", "Invalid email or password");
		}
		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully");
		}
		return "login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("userRegistrationDto", new UserRegistrationDto());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			return "register";
		}

		if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
			model.addAttribute("error", "Passwords do not match");
			return "register";
		}

		try {
			authService.register(userRegistrationDto);
			redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
			return "redirect:/login";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "register";
		}
	}
}