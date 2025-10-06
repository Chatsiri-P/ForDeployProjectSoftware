package com.example.demo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Category;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;
	private final EventService eventService;
	private final UserService userService;
	private final BookmarkService bookmarkService;

	@Autowired
	public CategoryController(EventService eventService, CategoryService categoryService, UserService userService,
			BookmarkService bookmarkService) {
		this.categoryService = categoryService;
		this.eventService = eventService;
		this.userService = userService;
		this.bookmarkService = bookmarkService;
	}

	@GetMapping
	public String categories(Model model, Principal principal,
			@RequestParam(value = "selected", required = false) Long selectedCategoryId) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);

			User currentUser = userService.getCurrentUser();
			model.addAttribute("currentUser", currentUser);
		if (selectedCategoryId != null) {
			Category selectedCategory = categoryService.findById(selectedCategoryId);
			List<Event> categoryEvents = categoryService.getEventsByCategory(selectedCategory);

			model.addAttribute("selectedCategory", selectedCategory);
			model.addAttribute("categoryEvents", categoryEvents);
			model.addAttribute("selectedCategoryId", selectedCategoryId);
			// Map เก็บ status ของแต่ละ event
			Map<Long, Boolean> isBookmarked = new HashMap<>();
			Map<Long, Boolean> canJoin = new HashMap<>();
			
			// Add bookmark status for each event
			categoryEvents.forEach(event -> {
				isBookmarked.put(event.getId(), bookmarkService.isBookmarked(currentUser, event));
				canJoin.put(event.getId(), eventService.canUserJoin(currentUser, event));
				event.getImagePath(); // Initialize lazy loading
			});
			model.addAttribute("isBookmarked", isBookmarked);
			model.addAttribute("canJoin", canJoin);
		}
		return "category";
	}

	@GetMapping("/{id}")
	public String categoryDetail(@PathVariable Long id, Model model, Principal principal) {
		return "redirect:/categories?selected=" + id;
	}
}