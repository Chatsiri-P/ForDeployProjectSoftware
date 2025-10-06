package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.EventDto;
import com.example.demo.dto.EventViewDto;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {
	private final EventService eventService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final CommentService commentService;
	private final BookmarkService bookmarkService;
	private final ImageService imageService;

	@Autowired
	public EventController(EventService eventService, CategoryService categoryService, UserService userService,
			CommentService commentService, BookmarkService bookmarkService, ImageService imageService) {
		this.eventService = eventService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.commentService = commentService;
		this.bookmarkService = bookmarkService;
		this.imageService = imageService;
	}

	@GetMapping("/{id}")
	public String eventDetail(@PathVariable Long id, Model model, Principal principal) {
		Event event = eventService.findEventById(id);
		User currentUser = null;
		

		if (principal != null) {
			currentUser = userService.getCurrentUser();
			EventViewDto status = new EventViewDto(
					event,
					bookmarkService.isBookmarked(currentUser, event),
                    eventService.canUserJoin(currentUser, event),
                    eventService.isPastEvent(event),
                    eventService.isUserMember(currentUser, event),
                    event.getCreator().getId().equals(currentUser.getId()));
			model.addAttribute("status", status);
			
		}

		model.addAttribute("event", event);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("comments", commentService.getEventComments(event));
		model.addAttribute("commentDto", new CommentDto());
		model.addAttribute("eventImages", imageService.getEventImages(event));

		return "event";
	}
	

    @GetMapping("/{id}/members")
    public String eventMembers(@PathVariable Long id, Model model, Principal principal) {
        Event event = eventService.findEventById(id);
        User currentUser = null;
        
        if (principal != null) {
            currentUser = userService.getCurrentUser();
        }
        
        model.addAttribute("event", event);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("members", eventService.getEventMembers(event));
        model.addAttribute("isOrganizer", currentUser != null && event.getCreator().equals(currentUser));
        
        return "event-members";
    }

    @PostMapping("/{eventId}/members/{userId}/remove")
    public String removeMember(@PathVariable Long eventId, 
                              @PathVariable Long userId,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        try {
            User currentUser = userService.getCurrentUser();
            Event event = eventService.findEventById(eventId);
            
            // Check if current user is the organizer
            if (!event.getCreator().equals(currentUser)) {
                throw new RuntimeException("Only the event organizer can remove members");
            }
            
            User memberToRemove = userService.findById(userId);
            eventService.removeMember(eventId, memberToRemove);
            
            redirectAttributes.addFlashAttribute("message", "Member removed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/events/" + eventId + "/members";
    }

    @GetMapping("/{id}/edit")
    public String editEventForm(@PathVariable Long id, Model model, Principal principal) {
        Event event = eventService.findEventById(id);
        User currentUser = userService.getCurrentUser();
        
        // Check if current user is the organizer
        if (!event.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the event organizer can edit this event");
        }
        
        EventDto eventDto = eventService.convertToDto(event);
        model.addAttribute("eventDto", eventDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("event", event);
        
        return "event-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                             @Valid @ModelAttribute EventDto eventDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             Principal principal) {
        
        Event event = eventService.findEventById(id);
        User currentUser = userService.getCurrentUser();
        
        // Check if current user is the organizer
        if (!event.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the event organizer can edit this event");
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("event", event);
            return "event-edit";
        }

        try {
            eventService.updateEvent(id, eventDto, imageFile);
            redirectAttributes.addFlashAttribute("message", "Event updated successfully!");
            return "redirect:/events/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("event", event);
            return "event-edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id, 
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        try {
            User currentUser = userService.getCurrentUser();
            Event event = eventService.findEventById(id);
            
            // Check if current user is the organizer
            if (!event.getCreator().equals(currentUser)) {
                throw new RuntimeException("Only the event organizer can delete this event");
            }
            
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("message", "Event deleted successfully!");
            return "redirect:/events/my-events";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events/" + id;
        }
    }

    @PostMapping("/{eventId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long eventId,
                               @PathVariable Long commentId,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {
        try {
            User currentUser = userService.getCurrentUser();
            commentService.deleteComment(commentId, currentUser);
            redirectAttributes.addFlashAttribute("message", "Comment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/events/" + eventId;
    }


	@GetMapping("/create")
	public String createEventForm(Model model) {
		model.addAttribute("eventDto", new EventDto());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "create-event";
	}

	@PostMapping("/create")
	public String createEvent(@Valid @ModelAttribute EventDto eventDto, BindingResult bindingResult,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryService.getAllCategories());
			return "create-event";
		}

		try {
			User currentUser = userService.getCurrentUser();
			Event event = eventService.createEvent(eventDto, currentUser, imageFile);
			redirectAttributes.addFlashAttribute("message", "Event created successfully!");
			return "redirect:/events/" + event.getId();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("categories", categoryService.getAllCategories());
			return "create-event";
		}
	}

	@PostMapping("/{id}/join")
	public String joinEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User currentUser = userService.getCurrentUser();
			eventService.joinEvent(id, currentUser);
			redirectAttributes.addFlashAttribute("message", "Successfully joined the event!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/events/" + id;
	}

	@PostMapping("/{id}/leave")
	public String leaveEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User currentUser = userService.getCurrentUser();
			eventService.leaveEvent(id, currentUser);
			redirectAttributes.addFlashAttribute("message", "Successfully left the event!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/events/" + id;
	}

	@PostMapping("/{id}/bookmark")
	public String bookmarkEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User currentUser = userService.getCurrentUser();
			Event event = eventService.findEventById(id);
			bookmarkService.addBookmark(currentUser, event);
			redirectAttributes.addFlashAttribute("message", "Event bookmarked!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/events/" + id;
	}

	@PostMapping("/{id}/unbookmark")
	public String unbookmarkEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User currentUser = userService.getCurrentUser();
			Event event = eventService.findEventById(id);
			bookmarkService.removeBookmark(currentUser, event);
			redirectAttributes.addFlashAttribute("message", "Bookmark removed!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/events/" + id;
	}

	@PostMapping("/{id}/comments")
	public String addComment(@PathVariable Long id, 
			@Valid @ModelAttribute("commentDto") CommentDto commentDto,
			BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Comment cannot be empty");
			return "redirect:/events/" + id;
		}

		try {
			commentDto.setEventId(id);
			User currentUser = userService.getCurrentUser();
			commentService.addComment(commentDto, currentUser);
			redirectAttributes.addFlashAttribute("message", "Comment added successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/events/" + id;
	}
	
	@PostMapping("/{id}/comments/delete")
	public String deleteComment (@PathVariable Long id, RedirectAttributes redirectAttributes) {
		return "redirect:/events/" + id;
	}

	@GetMapping("/my-events")
	public String myEvents(Model model) {
	    User currentUser = userService.getCurrentUser();

	    // ป้องกัน null ก่อนส่งไป Thymeleaf
	    List<Event> createdEvents = eventService.getEventsByCreator(currentUser);
	    List<Event> joinedEvents = eventService.getEventsByMember(currentUser);
	    List<Event> bookmarkedEvents = bookmarkService.getUserBookmarkedEvents(currentUser);

	    model.addAttribute("createdEvents", createdEvents != null ? createdEvents : new ArrayList<>());
	    model.addAttribute("joinedEvents", joinedEvents != null ? joinedEvents : new ArrayList<>());
	    model.addAttribute("bookmarkedEvents", bookmarkedEvents != null ? bookmarkedEvents : new ArrayList<>());

	    return "myevent";
	}

}