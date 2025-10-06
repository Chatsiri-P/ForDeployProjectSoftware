package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@GetMapping("/register")
	public String register(Model model) {
		return "register";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
}
