package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.EventDto;
import com.example.demo.model.*;
import com.example.demo.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventMemberRepository eventMemberRepository;
    private final EventRoleRepository eventRoleRepository;
    private final CategoryRepository categoryRepository;
    private final EventSubject eventSubject;

    @Value("${app.upload.event-dir}")
    private String eventUploadDir;

    @Autowired
    public EventService(EventRepository eventRepository,
                       EventMemberRepository eventMemberRepository,
                       EventRoleRepository eventRoleRepository,
                       CategoryRepository categoryRepository,
                       EventSubject eventSubject) {
        this.eventRepository = eventRepository;
        this.eventMemberRepository = eventMemberRepository;
        this.eventRoleRepository = eventRoleRepository;
        this.categoryRepository = categoryRepository;
        this.eventSubject = eventSubject;
    }

    public Event createEvent(EventDto eventDto, User creator, MultipartFile imageFile) {
        Event event = buildEventFromDto(eventDto, creator);
        
        // Handle single image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveEventImage(imageFile);
            event.setImagePath(imagePath);
        }
        
        Event savedEvent = eventRepository.save(event);
        addCreatorAsOrganizer(savedEvent, creator);
        
        return savedEvent;
    }
    
    public Event updateEvent(Long eventId, EventDto eventDto, MultipartFile imageFile) {
        Event event = findEventById(eventId);
        
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setLocation(eventDto.getLocation());
        event.setMaxParticipants(eventDto.getMaxParticipants());
        
        Category category = categoryRepository.findById(eventDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        event.setCategory(category);
        
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (event.getImagePath() != null) {
                deleteEventImage(event.getImagePath());
            }
            String imagePath = saveEventImage(imageFile);
            event.setImagePath(imagePath);
        }
        
        return eventRepository.save(event);
    }


    private String saveEventImage(MultipartFile file) {
        try {
            String filename = generateFileName(file.getOriginalFilename());
            String filePath = eventUploadDir + filename;
            
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private Event buildEventFromDto(EventDto eventDto, User creator) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setLocation(eventDto.getLocation());
        event.setMaxParticipants(eventDto.getMaxParticipants());
        event.setCreator(creator);
        
        Category category = categoryRepository.findById(eventDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        event.setCategory(category);
        
        return event;
    }

    private void addCreatorAsOrganizer(Event event, User creator) {
        EventRole organizerRole = eventRoleRepository.findByName("ORGANIZER")
                .orElseThrow(() -> new RuntimeException("Organizer role not found"));
        
        EventMember eventMember = new EventMember(creator, event, organizerRole);
        eventMemberRepository.save(eventMember);
    }
    
    public void deleteEvent(Long eventId) {
        Event event = findEventById(eventId);
        
        // Delete associated image
        if (event.getImagePath() != null) {
            deleteEventImage(event.getImagePath());
        }
        event.setDeleted(true);
        eventRepository.save(event);
    }

    private void deleteEventImage(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Log the error but don't fail the operation
            System.err.println("Failed to delete image: " + e.getMessage());
        }
    }
    
    public EventDto convertToDto(Event event) {
        EventDto dto = new EventDto();
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getDate());
        dto.setLocation(event.getLocation());
        dto.setMaxParticipants(event.getMaxParticipants());
        dto.setCategoryId(event.getCategory().getId());
        return dto;
    }
    

    public List<EventMember> getEventMembers(Event event) {
        return eventMemberRepository.findByEvent(event);
    }

    public void removeMember(Long eventId, User user) {
        Event event = findEventById(eventId);
        EventMember eventMember = eventMemberRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new RuntimeException("User is not a member of this event"));
        
        // Prevent removing the organizer
        if ("ORGANIZER".equals(eventMember.getEventRole().getName())) {
            throw new RuntimeException("Cannot remove the event organizer");
        }
        
        eventMemberRepository.delete(eventMember);
    }


    public void joinEvent(Long eventId, User user) {
        Event event = findEventById(eventId);
        
        if (event.isFull()) {
            throw new RuntimeException("Event is full");
        }
        
        if (eventMemberRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("User already joined this event");
        }
        
        EventRole participantRole = eventRoleRepository.findByName("PARTICIPANT")
                .orElseThrow(() -> new RuntimeException("Participant role not found"));
        
        EventMember eventMember = new EventMember(user, event, participantRole);
        eventMemberRepository.save(eventMember);
        
        // Notify event creator using Observer pattern
        eventSubject.notifyUserJoined(user, event);
    }

    public void leaveEvent(Long eventId, User user) {
        Event event = findEventById(eventId);
        EventMember eventMember = eventMemberRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new RuntimeException("User is not a member of this event"));
        
        // Prevent organizer from leaving their own event
        if ("ORGANIZER".equals(eventMember.getEventRole().getName())) {
            throw new RuntimeException("Event organizer cannot leave the event");
        }
        
        eventMemberRepository.delete(eventMember);
    }

    public List<Event> getAllUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDateTime.now());
    }

    public List<Event> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUpcomingEvents();
        }
        return eventRepository.searchEvents(keyword.trim(), LocalDateTime.now());
    }

    public List<Event> getEventsByCreator(User creator) {
        return eventRepository.findByCreator(creator);
    }

    public List<Event> getEventsByMember(User user) {
        return eventRepository.findEventsByMember(user);
    }

    public List<Event> getEventsByCategory(Long categoryId) {
        return eventRepository.findByCategoryAndUpcoming(categoryId, LocalDateTime.now());
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public boolean isPastEvent(Event event) {
        LocalDateTime now = LocalDateTime.now();
        return event.getDate().isBefore(now);
    }
    
    public boolean isUserMember(User user, Event event) {
        return eventMemberRepository.existsByUserAndEvent(user, event);
    }

    public boolean canUserJoin(User user, Event event) {
        return !event.isFull() && !isUserMember(user, event) && !event.getCreator().equals(user);
    }
}