package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.EventViewDto;
import com.example.demo.model.*;
import com.example.demo.service.*;

@Controller
public class HomeController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final BookmarkService bookmarkService;

    @Autowired
    public HomeController(EventService eventService, 
                         CategoryService categoryService,
                         UserService userService,
                         BookmarkService bookmarkService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal,
                      @RequestParam(value = "search", required = false) String searchKeyword,
                      @RequestParam(value = "category", required = false) Long categoryId) {
        
        List<Event> events;
        
        // Handle search
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            events = eventService.searchEvents(searchKeyword);
            model.addAttribute("searchKeyword", searchKeyword);
            model.addAttribute("searchPerformed", true);
        } 
        // Default - all events
        else {
            events = eventService.getAllUpcomingEvents();
        }
        
        model.addAttribute("events", events);
        List<EventViewDto> statuses = new ArrayList<>();
        
        if (principal != null) {
            User currentUser = userService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
            

            
            // Add bookmark status for each event
            events.forEach(event -> {
                    statuses.add(new EventViewDto(
                        event,
                        bookmarkService.isBookmarked(currentUser, event),
                        eventService.canUserJoin(currentUser, event),
                        eventService.isPastEvent(event),
                        eventService.isPastEvent(event),
                        event.getCreator().getId().equals(currentUser.getId())
                    ));
                event.getImagePath(); // Initialize lazy loading
            });
            model.addAttribute("statuses", statuses);
        }
        
        return "home";
    }
}
