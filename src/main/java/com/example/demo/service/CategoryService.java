package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Category;
import com.example.demo.model.Event;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(String name, String description, String iconClass) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("Category already exists");
        }
        return categoryRepository.save(new Category(name, description, iconClass));
    }

    public List<Event> getEventsByCategory(Category category) {
        return eventRepository.findByCategoryAndUpcoming(category, LocalDateTime.now());
    }

    public List<Event> getEventsByCategoryId(Long categoryId) {
        Category category = findById(categoryId);
        return getEventsByCategory(category);
    }
}
